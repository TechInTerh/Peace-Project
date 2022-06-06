package registry

import model.DroneReportModel._

import com.softwaremill.tagging._
import com.sksamuel.avro4s.{AvroSchema, RecordFormat}
import io.circe.generic.auto._
import org.apache.avro.Schema
import sttp.client3.circe._
import sttp.client3.{HttpURLConnectionBackend, _}


package object SchemaRegistry {

    type KeyRFTag
    type ValueRFTag

    type KeyRecordFormat[K] = RecordFormat[K] @@ KeyRFTag
    type ValueRecordFormat[V] = RecordFormat[V] @@ ValueRFTag

    val reportIdSchema: Schema = AvroSchema[Int]
    val droneReportSchema: Schema = AvroSchema[DroneReport]

    implicit val reportIdRF: KeyRecordFormat[Int] = RecordFormat[Int].taggedWith[KeyRFTag]
    implicit val droneReportRF: ValueRecordFormat[DroneReport] = RecordFormat[DroneReport].taggedWith[ValueRFTag]

    // Needed to have a correct request body format
    case class RegisterSchemaRequest(schema: String)

    def registerSchema() = {
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
                println(s"Registered new schema version on subject: $subject, response status code $code")
        }
    }
}
