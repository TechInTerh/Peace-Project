import io.confluent.kafka.serializers.{ AbstractKafkaAvroSerDeConfig, KafkaAvroSerializer }
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde
import java.util.Collections
import java.util.Properties
//import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization._
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.ForeachAction
import org.apache.kafka.streams.kstream.Printed
import org.apache.kafka.streams.processor.Processor
import org.apache.kafka.streams.scala.StreamsBuilder;
import org.apache.kafka.streams.scala.kstream.Consumed._
import scala.annotation.tailrec
import sttp.client3.{HttpURLConnectionBackend, _}
import org.apache.avro.generic.{ GenericData, GenericRecordBuilder, GenericRecord }
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.scala.ImplicitConversions._

object Main extends App {
    def send(value: String): Unit = {
        val backend = HttpURLConnectionBackend()
        basicRequest.post(uri"http://srvc_back:8080/alert")
                    .body(value)
                    .send(backend)
                    .code
    }

    val props = new Properties();
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pipe")
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092")
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, classOf[GenericAvroSerde])
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, classOf[GenericAvroSerde])
    props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://schema-registry:8081")

    val serdeConfig = Collections.singletonMap(
          AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,
          "http://schema-registry:8081")
          /*
    val valueGenericAvroSerde = new GenericAvroSerde()
    valueGenericAvroSerde.configure(serdeConfig, false)
    val keyGenericAvroSerde = new GenericAvroSerde()
    keyGenericAvroSerde.configure(serdeConfig, true)
    */
    val builder = new StreamsBuilder()
    //implicit val consumed = Consumed.if(keyGenericAvroSerde, valueGenericAvroSerde)
    implicit val genericAvroSerde: Serde[GenericRecord] = {
        val gas = new GenericAvroSerde
        val isKeySerde = false
        gas.configure(serdeConfig, false)
        gas
    }
    val source = builder.stream[String, GenericRecord]("drone-report")

    source.foreach((k,v)=>send(v.toString()))
    /*
    source.foreach(new ForeachAction[String, GenericRecord]() {
        override def apply(key: String, value: GenericRecord) : Unit = {
          send(value.toString())
        }
    })
    */

    val topology = builder.build();

    val streams = new KafkaStreams(topology, props)

    streams.start()

    Runtime.getRuntime.addShutdownHook(new Thread(() => {
        streams.close()
    }))

    @tailrec
    def run(): Unit = {
      run();
    }

    run();
}
