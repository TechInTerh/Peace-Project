import Dependencies._

name := "peace-project"
version := "0.1"

lazy val commonSettings = Seq(
  scalaVersion := "2.13.0",
  resolvers += "Confluent Maven Repository" at "https://packages.confluent.io/maven/"
)

lazy val root = (project in file(".")).aggregate(srvc_drone, srvc_analysis, srvc_alert)

lazy val srvc_drone = (project in file("srvc_drone"))
  .settings(commonSettings)
  .settings(
    name := "srvc_drone",
    libraryDependencies ++= Seq(Libs.kafkaClient, Libs.kafkaAvro, Libs.catsEffect, Libs.avro4sCore, Libs.sttp3Core, Libs.sttp3Circe, Libs.circeGeneric, Libs.smlTagging)
  )

lazy val srvc_analysis = (project in file("srvc_analysis"))
  .settings(commonSettings)
  .settings(
    name := "srvc_analysis",
    libraryDependencies ++= Seq(Libs.kafkaClient, Libs.kafkaAvro, Libs.catsEffect)
  )
  .dependsOn(srvc_drone)

lazy val srvc_alert = (project in file("srvc_alert"))
  .settings(commonSettings)
  .settings(
    name := "srvc_alert",
    libraryDependencies ++= Seq(Libs.kafkaStreamsScala, Libs.kafkaStreamsAvro, Libs.avro4sKafka)
  )
  .dependsOn(srvc_drone)
