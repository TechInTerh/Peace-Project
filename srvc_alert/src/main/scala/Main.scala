import java.util.Properties
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.processor.Processor
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Printed
import org.apache.kafka.streams.kstream.ForeachAction
import sttp.client3.{HttpURLConnectionBackend, _}
import scala.annotation.tailrec

object Main extends App {
    def send(value: String): Unit = {
        val backend = HttpURLConnectionBackend()
        basicRequest.post(uri"http://srvc_back:8080/alert")
                    .body(value)
                    .send(backend)
                    .code
    }

    val props = new Properties();
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pipe");
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

    val builder = new StreamsBuilder();
    val source = builder.stream("drone-report")

    source.foreach(new ForeachAction[String, String]() {
        override def apply(key: String, value: String) : Unit = {
          send(value)
        }
    })

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
