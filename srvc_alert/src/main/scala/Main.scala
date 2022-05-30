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

object Main extends App {
  def send(value: String) {
        val backend = HttpURLConnectionBackend()
        basicRequest
                .post(uri"http://localhost:8080/joke")
                //.header("Content-Type", "application/vnd.schemaregistry.v1+json")
                .body(value)
                .send(backend)
                .code
  }
  //send("other2")
    val props = new Properties();
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pipe");
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    // assuming that the Kafka broker this application is talking to runs on local machine with port 9092
    val builder = new StreamsBuilder();

    val source = builder
                    .stream("drone-report")
                    //.to("test")

    /*
    val sysout = Printed
      .toSysOut[Nothing, Nothing]
      .withLabel("customerStream")
    builder.print(sysout)
    */
    source.peek(new ForeachAction[String, String]() {
      override def apply(key: String, value: String) {
        send(value)
      }
    }).to("test")
    val topology = builder.build();
    println(topology.describe())

     val streams = new KafkaStreams(topology, props)

  streams.start()

  Runtime.getRuntime.addShutdownHook(new Thread(() => {
  streams.close()
}))

  while (true) {
  }
}