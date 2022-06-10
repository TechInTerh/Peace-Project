import io.confluent.kafka.serializers.{ AbstractKafkaAvroSerDeConfig, KafkaAvroSerializer }
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde
import java.util.Collections
import java.util.Properties
import org.apache.avro.generic.{ GenericData, GenericRecordBuilder, GenericRecord }
import org.apache.kafka.clients.consumer._
import org.apache.kafka.common.serialization._
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream.ForeachAction
import org.apache.kafka.streams.kstream.Printed
import org.apache.kafka.streams.processor.Processor
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.Consumed._
import org.apache.kafka.streams.scala.serialization.Serdes._
import scala.annotation.tailrec
import scala.jdk.javaapi.CollectionConverters
import sttp.client3.{HttpURLConnectionBackend, _}

object Main extends App {
    def send(value: String): Unit = {
        val backend = HttpURLConnectionBackend()
        basicRequest.post(uri"http://srvc_notif:8080/alert")
                    .body(value)
                    .send(backend)
                    .code
    }

    def postAlert(name: String, lat: String, lon: String): Unit = {
        val value: String = "{\"name\": \"" + name +
                            "\", \"lat\": " + lat +
                            ", \"lon\": " + lon +"}"

        val backend = HttpURLConnectionBackend()
        basicRequest.post(uri"http://srvc_back:8080/alert")
                    .header("Content-Type", "application/json")
                    .body(value)
                    .send(backend)
                    .code
    }
    val props = new Properties()
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pipe3")
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092")

    val serdeConfig = Collections.singletonMap(
          AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,
          "http://schema-registry:8081"
        )

    val builder = new StreamsBuilder()
    implicit val genericAvroSerde: Serde[GenericRecord] = {
        val gas = new GenericAvroSerde
        val isKeySerde = false
        gas.configure(serdeConfig, false)
        gas
    }
    val source = builder.stream[String, GenericRecord]("drone-report")

    def citizensToSeq(citizens: Object) = {
      val array = citizens.asInstanceOf[GenericData.Array[GenericData.Record]]
      CollectionConverters
          .asScala(array.iterator()).toSeq
    }
    val SCORE_TRESHOLD = 50
    source.peek((k,v)=>println(v.toString()))
    val ks1 = source.flatMapValues(x=>citizensToSeq(x.get("citizens"))
      .map(c=>(c, x.get("latitude"), x.get("longitude"))))

    ks1.peek((k,v)=>println(v.toString()))
    val ks2 = ks1.filter((k,v)=>v._1
                                 .get("peaceScore")
                                 .asInstanceOf[Integer] > SCORE_TRESHOLD)
                 .foreach((k,v)=>postAlert(v._1
                                            .get("name")
                                            .toString(),
                                           v._2.toString(),
                                           v._3.toString()))

    val topology = builder.build()
    println(topology.describe())

    val streams = new KafkaStreams(topology, props)

    streams.start()

    Runtime.getRuntime.addShutdownHook(new Thread(() => {
        streams.close()
    }))

    @tailrec
    def run(): Unit = {
        run()
    }

    run()
}
