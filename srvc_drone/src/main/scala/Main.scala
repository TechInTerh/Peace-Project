import model.DroneReportModel._
import registry.SchemaRegistry._

import io.confluent.kafka.serializers.{ AbstractKafkaAvroSerDeConfig, KafkaAvroSerializer }
import java.util.Properties
import org.apache.avro.generic.{ GenericData, GenericRecord }
import org.apache.kafka.clients.producer._
import scala.jdk.CollectionConverters._

import scala.util.{ Failure, Success }

object Main extends App {

    val TOPIC_NAME = "drone-report"
    val NB_DRONES = 10
    val NB_REPORTS_PER_DRONE = 100

    println("Start registering AVRO schemas ...")
    registerSchema()
    println("Finish registering AVRO schemas !")

    println(s"Start producing $NB_REPORTS_PER_DRONE records on $TOPIC_NAME ...")

    val props: Map[String, Object] = Map(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG -> "kafka:9092",
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG -> classOf[KafkaAvroSerializer],
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG -> classOf[KafkaAvroSerializer],
        AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG -> "http://schema-registry:8081"
    )

    val producer = new KafkaProducer[Int, GenericRecord](props.asJava)

    (1 to NB_REPORTS_PER_DRONE).foreach { reportId =>
        val wordList = Vector("word1", "word2", "word3", "word4", "word5")

        val citizen = new GenericData.Record(citizenSchema)
        citizen.put("name", "Patrick")
        citizen.put("peaceScore", 15)

        val droneReport = new GenericData.Record(droneReportSchema)
        droneReport.put("droneId", 4)
        droneReport.put("latitude", 10.5)
        droneReport.put("longitude", 10.5)
        droneReport.put("timestamp", System.currentTimeMillis())
        droneReport.put("citizens", Vector(citizen).toArray)
        droneReport.put("words", wordList.toArray)

        val record = new ProducerRecord[Int, GenericRecord](TOPIC_NAME, reportId, droneReport)
        producer.send(record)
    }

    producer.close()

    println(s"Successfully produced $NB_REPORTS_PER_DRONE records on $TOPIC_NAME !")
}
