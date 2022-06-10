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
	val sttp3 = "3.3.11"
	val smlCommon = "2.3.1"

    val spark = "3.2.0"
    val hadoop = "3.3.1"
    val amazonAws = "1.11.234"
    val joda = "2.9.9"
    val httpComponent = "4.5.3"
  }

  object Libs {
    val kafkaClient = "org.apache.kafka" % "kafka-clients" % V.kafka
    val kafkaStreams = "org.apache.kafka" % "kafka-streams" % V.kafka
    val kafkaStreamsScala = "org.apache.kafka" %% "kafka-streams-scala" % V.kafka
    val kafkaAvro = "io.confluent" % "kafka-avro-serializer" % V.kafkaAvro
    val kafkaStreamsAvro = "io.confluent" % "kafka-streams-avro-serde" % V.kafkaAvro
    val avro4sCore = "com.sksamuel.avro4s" % "avro4s-core_2.13" % V.avro4s
    val avro4sKafka = "com.sksamuel.avro4s" % "avro4s-kafka_2.13" % V.avro4s
    val sttp3Core = "com.softwaremill.sttp.client3" %% "core" % V.sttp3
    val sttp3Circe = "com.softwaremill.sttp.client3" %% "circe" % V.sttp3
    val circeGeneric = "io.circe" %% "circe-generic" % V.circe
    val catsEffect = "org.typelevel" %% "cats-effect" % V.cats
    val smlTagging = "com.softwaremill.common" %% "tagging" % V.smlCommon
    val http4sEmberServer = "org.http4s" %% "http4s-ember-server" % V.http4sVersion
    val http4sEmberClient = "org.http4s" %% "http4s-ember-client" % V.http4sVersion
    val https4sCirce = "org.http4s" %% "http4s-circe" % V.http4sVersion
    val http4Sdsl = "org.http4s" %% "http4s-dsl" % V.http4sVersion
    val logback = "ch.qos.logback" % "logback-classic" % V.logbackVersion % Runtime

    // analysis
    val sparkCore = "org.apache.spark" %% "spark-core" % V.spark
    val sparkSql = "org.apache.spark" %% "spark-sql" % V.spark
    val sparkAvro = "org.apache.spark" %% "spark-avro" % V.spark
    val hadoopClient = "org.apache.hadoop" % "hadoop-client" % V.hadoop
    val hadoopCommon = "org.apache.hadoop" % "hadoop-aws" % V.hadoop
    val amazonAws = "com.amazonaws" % "aws-java-sdk-core" % V.amazonAws
    val amazonAwsS3 = "com.amazonaws" % "aws-java-sdk-s3" % V.amazonAws
    val amazonAxsKms = "com.amazonaws" % "aws-java-sdk-kms" % V.amazonAws
    val smlCommon = "2.3.1"
    val sttp3 = "3.3.11"
  }
}
