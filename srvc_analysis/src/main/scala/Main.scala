import Main.avrotestpeace

import java.util.Properties
import org.apache.avro.reflect.AvroSchema
import org.apache.hadoop.conf.Configuration
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.hadoop.fs.{FileSystem, Path}

import java.io.{BufferedWriter, File, FileWriter}

object Main extends App {

	def mean(xs: Iterable[Int]) = xs.sum / xs.size

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

	val avrotest = spark.read.format("avro").load("s3a://kafka-bucket/topics/drone-report/partition=0/*.avro")

	val file = new File("rendu.txt")
	val bw = new BufferedWriter(new FileWriter(file))
	bw.write("Registry of saved Peacewatcher reports : ")
	bw.newLine()

	//Question 1 : combien de messages de drones avons nous ?

	bw.write("Number of reports registered = ")

	bw.write(avrotest.distinct().count().toString)

	bw.newLine()

	//Question 2 : à quelles heures avons nous le plus de rapports ?

	var avrotesttimestamphour = avrotest.withColumn("hour", (col("timestamp") % 86400) / 3600)

	avrotesttimestamphour = avrotesttimestamphour.withColumn("hour", col("hour").cast("int"))

	val avrotesttimestampgroup = avrotesttimestamphour.groupBy("hour").count().sort(desc("count"))

	avrotesttimestampgroup.repartition(1).write.option("header",true).csv("messagesperhour")

	bw.newLine()

	bw.write("To see number of messages sent by hour, see file messageperhour")
	bw.newLine()

	//Question 3 : Quels drones ont envoyé le plus de rapports ?
	val avrotestdronegroup = avrotest.groupBy("droneId").count().sort(desc("count"))
	avrotestdronegroup.show()
	println(avrotestdronegroup.count())

	//Mes tests pour obtenir le peacescore
	//val avrotestexploded = avrotest.withColumn("citizen", explode(col("citizens")))
	//avrotestexploded.withColumn("peacescore",col("citizen.peaceScore")).show()

	//val avrotestdronegroup2 = avrotest.groupBy("droneId").sum("citizens.peaceScore")
	//avrotest.printSchema()

	//Question 4 : quelles sont les
	var avrotestpeace = avrotest.withColumn("peaceScores", col("citizens.peaceScore"))
	avrotestpeace = avrotestpeace.withColumn("peaceScoresum", aggregate(col("peaceScores"), lit(0), (x, y) => x + y))
	avrotestpeace = avrotestpeace.withColumn("peaceScoremax", array_max(col("peaceScores")))
	avrotestpeace = avrotestpeace.withColumn("peaceScoremin", array_min(col("peaceScores")))
	avrotestpeace = avrotestpeace.withColumn("numbercitizens", size(col("citizens.peaceScore")))
	avrotestpeace = avrotestpeace.withColumn("peaceScoremean", col("peaceScoresum") / col("numbercitizens"))

	avrotestpeace.groupBy(col("droneId")).avg("peaceScoremean")
	//avrotestpeace = avrotestpeace.withColumn("peaceScoremedian", col("peaceScoresum") / col("numbercitizens"))
	//avrotestpeace = avrotestpeace.withColumn("Day", to_date(col("timestamp")))

	avrotestpeace.show()


	//avrotestdronegroup2.show()
	//avrotest.withColumn("citizensstring", array_join(avrotest.col("words"),"|")).show()

	//new FileWriter("renvoi.txt")

	bw.close()
	println("FIN !")
}
