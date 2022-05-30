import java.util.Properties
import org.apache.kafka.clients.consumer._
import org.apache.kafka.common.serialization.StringDeserializer

import java.time.Duration
import scala.collection.JavaConverters

object Main extends App {

  val props: Properties = new Properties()
  props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "broker1:9092")
  props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
  props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
  props.put(ConsumerConfig.GROUP_ID_CONFIG, classOf[StringDeserializer])

  val consumer = new KafkaConsumer[String, String](props)
  //consumer.subscribe(List("topicname").asJava)

  val records: ConsumerRecords[String, String] = consumer.poll(Duration.ofMillis(100))

  //records.asScala.foreach {record => println(s"Print data here")}

  //consumer.subscribe()

  //val jsonString = os.read(os.pwd/"src"/"test"/"resources"/"phil.json")
  //val data = ujson.read(jsonString)
  //data.value // LinkedHashMap("first_name" -> Str("Phil"), "last_name" -> Str("Hellmuth"), "birth_year" -> Num(1964.0))
}
