import com.sksamuel.avro4s.{AvroSchema, RecordFormat}
import com.softwaremill.tagging._
import io.circe.generic.auto._
import org.apache.avro.Schema
import sttp.client3.circe._
import sttp.client3.{HttpURLConnectionBackend, _}
import java.util.Date

object RegisterSchema extends App {

    // Define the data format of our drones reports
    case class ReportId(value: Int)

    case class Citizen(name: String, peaceScore: Int)

    case class DroneReport(
        droneId: Int,
        latitude: Double,
        longitude: Double,
        citizens: Vector[Citizen],
        words: Vector[String],
        // Avro cringe with Date type, so we'll just use String and set a format
        timestamp: String
    )

    // Define our Avro Schemas before registering them with the Avro Registry
    type KeyRFTag
    type ValueRFTag

    type KeyRecordFormat[K] = RecordFormat[K] @@ KeyRFTag
    type ValueRecordFormat[V] = RecordFormat[V] @@ ValueRFTag

    val reportIdSchema: Schema = AvroSchema[ReportId]
    val droneReportSchema: Schema = AvroSchema[DroneReport]

    implicit val reportIdRF: KeyRecordFormat[ReportId] = RecordFormat[ReportId].taggedWith[KeyRFTag]
    implicit val droneReportRF: ValueRecordFormat[DroneReport] = RecordFormat[DroneReport].taggedWith[ValueRFTag]

    // Register our format in the schema Registry with API endpoints calls
    case class RegisterSchemaRequest(schema: String)

    val backend = HttpURLConnectionBackend()

    Seq(
        ("drone-report-key", RegisterSchemaRequest(reportIdSchema.toString())),
        ("drone-report-value", RegisterSchemaRequest(droneReportSchema.toString()))
    ).map {
        case (subject, schema) =>
            subject -> basicRequest
                .post(uri"http://schema-registry:8081/subjects/$subject/versions")
                .header("Content-Type", "application/vnd.schemaregistry.v1+json")
                .body(schema)
                .send(backend)
                .code
    }.foreach {
        case (subject, code) =>
            println(s"Register schema $subject, response status code $code")
    }
}
