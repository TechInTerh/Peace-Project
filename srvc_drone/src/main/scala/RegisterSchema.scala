package registry

import model.DroneReportModel._

import com.softwaremill.tagging._
import com.sksamuel.avro4s.{AvroSchema, RecordFormat}
import scala.language.higherKinds
import io.circe.generic.auto._
import org.apache.avro.Schema
import sttp.client3.circe._
import sttp.client3.{HttpURLConnectionBackend, _}


package object SchemaRegistry {

    type ValueRFTag

    type ValueRecordFormat[V] = RecordFormat[V] @@ ValueRFTag

    val droneReportSchema: Schema = AvroSchema[DroneReport]

    implicit val droneReportRF: ValueRecordFormat[DroneReport] = RecordFormat[DroneReport].taggedWith[ValueRFTag]

    // Needed to have a correct request body format
    case class RegisterSchemaRequest(schema: String)

    def registerSchema() = {
        val backend = HttpURLConnectionBackend()
        val subject = "drone-report-value"
        val responseCode = basicRequest
            .post(uri"http://schema-registry:8081/subjects/$subject/versions")
            .header("Content-Type", "application/vnd.schemaregistry.v1+json")
            .body(RegisterSchemaRequest(droneReportSchema.toString()))
            .send(backend)
            .code

        println(s"Registered new schema version for $subject, response status code $responseCode")
    }
}
