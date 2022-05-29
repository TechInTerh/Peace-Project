import java.util.Properties
import org.apache.kafka.clients.producer._

object Main extends App {

    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](props)
    val TOPIC = "test"

    for (i <- 1 to 50) {
        val record = new ProducerRecord[String, String](TOPIC, "key", s"hello $i")
        producer.send(record)
    }

    val record = new ProducerRecord[String, String](TOPIC, "key", "the end" + new java.util.Date)
    producer.send(record)
    producer.close()
}