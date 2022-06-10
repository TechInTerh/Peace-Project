import sbt._

object Dependencies {

  object V {
    val avro4s = "4.0.10"
    val cats = "3.2.5"
    val circe = "0.14.1"
    val circeVersion = "0.14.2"
    val http4sVersion = "0.23.12"
    val kafka = "2.8.0"
    val kafkaAvro = "6.2.0"
    val logbackVersion = "1.2.10"
    val smlCommon = "2.3.1"
    val sttp3 = "3.3.11"
  }

  object Libs {
    val avro4sCore = "com.sksamuel.avro4s" % "avro4s-core_2.13" % V.avro4s
    val avro4sKafka = "com.sksamuel.avro4s" % "avro4s-kafka_2.13" % V.avro4s
    val catsEffect = "org.typelevel" %% "cats-effect" % V.cats
    val circeGeneric = "io.circe" %% "circe-generic" % V.circe
    val kafkaAvro = "io.confluent" % "kafka-avro-serializer" % V.kafkaAvro
    val kafkaClient = "org.apache.kafka" % "kafka-clients" % V.kafka
    val kafkaStreams = "org.apache.kafka" % "kafka-streams" % V.kafka
    val kafkaStreamsAvro = "io.confluent" % "kafka-streams-avro-serde" % V.kafkaAvro
    val kafkaStreamsScala = "org.apache.kafka" %% "kafka-streams-scala" % V.kafka
    val smlTagging = "com.softwaremill.common" %% "tagging" % V.smlCommon
    val sttp3Circe = "com.softwaremill.sttp.client3" %% "circe" % V.sttp3
    val sttp3Core = "com.softwaremill.sttp.client3" %% "core" % V.sttp3
  }
}
