import model.DroneReportModel._
import registry.SchemaRegistry._

import java.util.Properties
import org.apache.kafka.clients.producer._

object Main extends App {

    val TOPIC_NAME = "drone-report"
    val NB_RECORDS = 100

    println("Start registering AVRO schemas ...")
    registerSchema()
    println("Finish registering AVRO schemas !")

    println(s"Start producing $NB_RECORDS records on $TOPIC_NAME ...")

    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("schema.resgistry.url", "http://schema-registry:8081")

    val producer = new KafkaProducer[String, String](props)

    for (i <- 1 to 50) {
        val record = new ProducerRecord[String, String](TOPIC_NAME, "key", s"hello $i")
        producer.send(record)
    }

    val record = new ProducerRecord[String, String](TOPIC_NAME, "key", "the end" + new java.util.Date)
    producer.send(record)
    producer.close()

    println(s"Successfully produced $NB_RECORDS records on $TOPIC_NAME !")
}
