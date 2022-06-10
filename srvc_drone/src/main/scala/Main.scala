import model.DroneReportModel._
import registry.SchemaRegistry._

import com.sksamuel.avro4s.AvroSchema
import scala.language.higherKinds
import io.confluent.kafka.serializers.{ AbstractKafkaAvroSerDeConfig, KafkaAvroSerializer }
import java.time._
import java.util.Properties
import org.apache.avro.{ Schema }
import org.apache.avro.generic.{ GenericData, GenericRecordBuilder, GenericRecord }
import org.apache.kafka.clients.producer._
import scala.util.Random
import scala.annotation.tailrec
import scala.jdk.CollectionConverters._
import scala.util.{ Failure, Success }
import scala.jdk.javaapi.CollectionConverters

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
    val NB_SECONDS = 60

    // Schema initialization
    println("Start registering AVRO schemas ...")
    registerSchema()
    println("Finish registering AVRO schemas !")

    println(s"Start producing records on $TOPIC_NAME ...")

    // Instantiate a producer with proper parameters
    val props = new Properties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer]);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer]);
    props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://schema-registry:8081");

    val producer = new KafkaProducer[GenericRecord](props)

    val citizenNames = List(
        "Adrien", "Alain", "Alexandre", "Timothée", "Victor",
        "Camille", "Lea", "Patricia", "Chloé", "Juliette"
    )

    val dictionary = List(
        "Fight", "Government", "Peace", "Peacefull", "PeaceLand",
        "PeaceWatcher", "Revolution", "Terrorism", "Working", "War"
    )

    @tailrec
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
        (1 to (1 + Random.nextInt(MAX_REPORTS_PER_DRONE))).foreach { reportId =>
            // Select random number of words and citizens
            val selectedWords = selectRandomElements(dictionary, List(), 1 + Random.nextInt(MAX_WORDS_PER_REPORT))
            val selectedCitizens = selectRandomElements(citizenNames, List(), 1 + Random.nextInt(MAX_CITIZENS_PER_REPORT)).map {
                new GenericRecordBuilder(AvroSchema[Citizen]).set("name", _)
                                                             .set("peaceScore", Random.nextInt(MAX_PEACE_SCORE))
                                                             .build()
            }

            // Add them in the correct array structs
            val words = new GenericData.Array[String](Schema.createArray(AvroSchema[String]), selectedWords.asJava);
            val citizens = new GenericData.Array[GenericData.Record](Schema.createArray(AvroSchema[Citizen]), selectedCitizens.asJava);

            // Get the report timestamp
            val timestamp = LocalDateTime.now.plusMinutes(5 * reportId)
                                             .plusSeconds(Random.nextInt(NB_SECONDS))
                                             .toEpochSecond(ZoneOffset.of("+01:00"))

            // Create a record and fill it with our random datas
            val droneReport = new GenericRecordBuilder(droneReportSchema)
                                .set("droneId", droneId)
                                .set("latitude", Random.nextDouble() * MAX_LATITUDE)
                                .set("longitude", Random.nextDouble() * MAX_LONGITUDE)
                                .set("timestamp", timestamp)
                                .set("words", words)
                                .set("citizens", citizens)
                                .build()
            producer.send(new ProducerRecord[GenericRecord](TOPIC_NAME, droneReport))
        }
    }

    producer.close()

    println(s"Successfully produced records on $TOPIC_NAME !")
}
