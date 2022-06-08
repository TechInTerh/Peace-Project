import model.DroneReportModel._
import registry.SchemaRegistry._

import com.sksamuel.avro4s.AvroSchema
import scala.language.higherKinds
import io.confluent.kafka.serializers.{ AbstractKafkaAvroSerDeConfig, KafkaAvroSerializer }
import java.util.Properties
import org.apache.avro.{ Schema }
import org.apache.avro.generic.{ GenericData, GenericRecordBuilder, GenericRecord }
import org.apache.kafka.clients.producer._
import scala.util.Random
import scala.jdk.CollectionConverters._

import scala.util.{ Failure, Success }

object Main extends App {

    // Service parameters
    val TOPIC_NAME = "drone-report"
    val NB_DRONES = 10
    val MAX_REPORTS_PER_DRONE = 100
    val MAX_PEACE_SCORE = 100
    val MAX_LONGITUDE = 150.0
    val MAX_LATITUDE = 200.0
    val MAX_CITIZENS_PER_REPORT = 3
    val MAX_WORDS_PER_REPORT = 3

    // Schema initialization
    println("Start registering AVRO schemas ...")
    registerSchema()
    println("Finish registering AVRO schemas !")

    println(s"Start producing records on $TOPIC_NAME ...")

    // Instantiate a producer with proper parameters
    val props: Map[String, Object] = Map(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG -> "kafka:9092",
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG -> classOf[KafkaAvroSerializer],
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG -> classOf[KafkaAvroSerializer],
        AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG -> "http://schema-registry:8081"
    )

    val producer = new KafkaProducer[Int, GenericRecord](props.asJava)

    val citizenNames = List(
        "Adrien", "Alain", "Alexandre", "Timothée", "Victor",
        "Camille", "Lea", "Patricia", "Chloé", "Juliette"
    )

    val dictionary = List(
        "Fight", "Government", "Peace", "Peacefull", "PeaceLand",
        "PeaceWatcher", "Revolution", "Terrorism", "Working", "War"
    )

    def selectRandomElements(srcList: List[String], dstList: List[String], nbElement: Int) : List[String] = {
        if (nbElement == 0) {
            return dstList
        }

        val elt = srcList(Random.nextInt(srcList.length))
        if (dstList.contains(elt)) {
            selectRandomElements(srcList, dstList, nbElement)
        } else {
            selectRandomElements(srcList, dstList :+ elt, nbElement - 1)
        }
    }

    (1 to NB_DRONES).foreach { droneId =>
        (1 to Random.nextInt(MAX_REPORTS_PER_DRONE)).foreach { reportId =>
            // Needed to create a record with AVRO schemas
            val words = new GenericData.Array[String](MAX_WORDS_PER_REPORT, Schema.createArray(AvroSchema[String]));
            val citizens = new GenericData.Array[GenericData.Record](citizenNames.length, Schema.createArray(AvroSchema[Citizen]));

            // Add random words
            selectRandomElements(dictionary, List(), Random.nextInt(MAX_WORDS_PER_REPORT)).foreach { words.add(_) }

            // Add random citizens to our record
            selectRandomElements(citizenNames, List(), Random.nextInt(MAX_CITIZENS_PER_REPORT)).map {
                new GenericRecordBuilder(AvroSchema[Citizen]).set("name", _)
                                                             .set("peaceScore", Random.nextInt(MAX_PEACE_SCORE))
                                                             .build()
            }.foreach { citizens.add(_) }

            // Create a record and fill it with random data
            val droneReport = new GenericData.Record(droneReportSchema)
            droneReport.put("droneId", droneId)
            droneReport.put("latitude", Random.nextDouble() * MAX_LATITUDE)
            droneReport.put("longitude", Random.nextDouble() * MAX_LONGITUDE)
            droneReport.put("timestamp", System.currentTimeMillis())
            droneReport.put("words", words)
            droneReport.put("citizens", citizens)

            val record = new ProducerRecord[Int, GenericRecord](TOPIC_NAME, reportId, droneReport)
            producer.send(record)
        }
    }

    producer.close()

    println(s"Successfully produced records on $TOPIC_NAME !")
}
