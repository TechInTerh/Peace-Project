import java.util.Properties
import org.apache.avro.reflect.AvroSchema
import org.apache.hadoop.conf.Configuration
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.hadoop.fs.{FileSystem, Path}

import java.io.{BufferedWriter, File, FileWriter}
import scala.reflect.io.Directory

object Main extends App {

	def mean(xs: Iterable[Int]) = xs.sum / xs.size

	val directory1 = new Directory(new File("messagesPerHour"))
	directory1.deleteRecursively()
	val directory2 = new Directory(new File("messagesPerDrone"))
	directory2.deleteRecursively()
	val directory3 = new Directory(new File("scorePerDrone"))
	directory3.deleteRecursively()

	val spark = SparkSession.builder().appName("Peace-Analyzer")
		.master("local[4]")
		.config("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
		.config("fs.s3a.endpoint", "http://minio:9000")
		.config("fs.s3a.access.key", "minioadmin")
		.config("fs.s3a.secret.key", "minioadmin")
		.config("fs.s3a.path.style.access", "true")
		.config("fs.s3a.multipart.size", "10485760")
		.config("fs.s3a.fast.upload", "true")
		.getOrCreate()

	val df = spark.read.format("avro").load("s3a://kafka-bucket/topics/drone-report/partition=0/*.avro")

	val file = new File("rendu.txt")
	val bufferWriter = new BufferedWriter(new FileWriter(file))
	bufferWriter.write("Registry of saved Peacewatcher reports : ")
	bufferWriter.newLine()

	//Question 1 : combien de messages de drones avons nous ?
	bufferWriter.write("1) Number of reports registered = ")
	bufferWriter.write(df.distinct().count().toString)
	bufferWriter.newLine()

	bufferWriter.write("Number of drones registered = ")
	bufferWriter.write(df.groupBy("droneId").count().count().toString)
	bufferWriter.newLine()

	//Question 2 : à quelles heures avons nous le plus de rapports ?
	var dfTimestampHour = df.withColumn("hour", (col("timestamp") % 86400) / 3600)
	dfTimestampHour = dfTimestampHour.withColumn("hour", col("hour").cast("int"))

	val dfTimestampGroup = dfTimestampHour.groupBy("hour").count().sort(desc("count"))
	dfTimestampGroup.repartition(1).write.option("header", true).csv("messagesPerHour")

	bufferWriter.write("2) To see number of messages sent by hour, see directory messagesPerHour, which contains a csv file")
	bufferWriter.newLine()

	//Question 3 : Quels drones ont envoyé le plus de rapports ?
	val dfDroneGroup = df.groupBy("droneId").count().sort(desc("count"))
	dfDroneGroup.repartition(1).write.option("header", true).csv("messagesPerDrone")
	bufferWriter.write("3) To see number of messages sent by each drone, see directory messagesPerDrone, which contains a csv file detailed")
	bufferWriter.newLine()

	//Question 4 : quelles sont les moyennes de peacescores relevées par chaque drone ?
	var dfPeaceScore = df.withColumn("peaceScores", col("citizens.peaceScore"))
	dfPeaceScore = dfPeaceScore.withColumn("peaceScoreSum", aggregate(col("peaceScores"), lit(0), (x, y) => x + y))
	dfPeaceScore = dfPeaceScore.withColumn("peaceScoreMax", array_max(col("peaceScores")))
	dfPeaceScore = dfPeaceScore.withColumn("peaceScoreMin", array_min(col("peaceScores")))
	dfPeaceScore = dfPeaceScore.withColumn("numberCitizens", size(col("citizens.peaceScore")))
	dfPeaceScore = dfPeaceScore.withColumn("peaceScoreMean", col("peaceScoreSum") / col("numberCitizens"))
	dfPeaceScore = dfPeaceScore.groupBy(col("droneId")).avg("peaceScoreMean")
	dfPeaceScore.repartition(1).write.option("header", true).csv("scorePerDrone")

	bufferWriter.write("4) To see the mean of peacescores retrieved by each drone, see directory messagesPerDrone, which contains a csv file detailed")
	bufferWriter.newLine()
	bufferWriter.close()
}
