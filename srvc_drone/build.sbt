name := "myApp"
version := "0.1"
scalaVersion := "2.11.12"
val sparkVersion = "2.3.0"

libraryDependencies ++= Seq(
"org.apache.bahir" %% "spark-streaming-twitter" % sparkVersion,
"org.apache.spark" %% "spark-core" % sparkVersion,
"org.apache.spark" % "spark-sql_2.11" % sparkVersion,
"org.apache.spark" %% "spark-streaming" % sparkVersion,
"org.apache.kafka" % "kafka_2.11" % "1.1.1"
)