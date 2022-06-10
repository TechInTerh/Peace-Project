import Main.avrotestpeace

import java.util.Properties
import org.apache.avro.reflect.AvroSchema
import org.apache.hadoop.conf.Configuration
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.hadoop.fs.{FileSystem, Path}

import java.io.{BufferedWriter, File, FileWriter}

object Main extends App {

	def mean(xs: Iterable[Int]) = xs.sum / xs.size

	/*
	def sparktocsv (sparkdf : DataFrame, name : String) = {
		for (row <- sparkdf) for (rowbis <- row) println(rowbis)
	}*/

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

	bw.write("1) Number of reports registered = ")
	bw.write(avrotest.distinct().count().toString)
	bw.newLine()

	bw.write("Number of drones registered = ")
	bw.write(avrotest.groupBy("droneId").count().count().toString)
	bw.newLine()


	//check
	//Question 2 : à quelles heures avons nous le plus de rapports ?

	//sparktocsv(avrotest.groupBy("droneId").count(),"suce.csv")

	var avrotesttimestamphour = avrotest.withColumn("hour", (col("timestamp") % 86400) / 3600)

	avrotesttimestamphour = avrotesttimestamphour.withColumn("hour", col("hour").cast("int"))

	val avrotesttimestampgroup = avrotesttimestamphour.groupBy("hour").count().sort(desc("count"))

	avrotesttimestampgroup.repartition(1).write.option("header",true).csv("messagesperhour")

	bw.write("2) To see number of messages sent by hour, see directory messageperhour, which contains a csv file")
	bw.newLine()

	//Question 3 : Quels drones ont envoyé le plus de rapports ?
	val avrotestdronegroup = avrotest.groupBy("droneId").count().sort(desc("count"))
	avrotestdronegroup.repartition(1).write.option("header",true).csv("messagesperdrone")
	bw.write("3) To see number of messages sent by each drone, see directory messageperdrone, which contains a csv file detailed")
	bw.newLine()

	//Mes tests pour obtenir le peacescore
	//val avrotestexploded = avrotest.withColumn("citizen", explode(col("citizens")))
	//avrotestexploded.withColumn("peacescore",col("citizen.peaceScore")).show()

	//val avrotestdronegroup2 = avrotest.groupBy("droneId").sum("citizens.peaceScore")
	//avrotest.printSchema()

	//Question 4 : quelles sont les moyennes de peacescores relevées par chaque drone ?
	var avrotestpeace = avrotest.withColumn("peaceScores", col("citizens.peaceScore"))
	avrotestpeace = avrotestpeace.withColumn("peaceScoresum", aggregate(col("peaceScores"), lit(0), (x, y) => x + y))
	avrotestpeace = avrotestpeace.withColumn("peaceScoremax", array_max(col("peaceScores")))
	avrotestpeace = avrotestpeace.withColumn("peaceScoremin", array_min(col("peaceScores")))
	avrotestpeace = avrotestpeace.withColumn("numbercitizens", size(col("citizens.peaceScore")))
	avrotestpeace = avrotestpeace.withColumn("peaceScoremean", col("peaceScoresum") / col("numbercitizens"))
	avrotestpeace = avrotestpeace.groupBy(col("droneId")).avg("peaceScoremean")
	//avrotestpeace = avrotestpeace.withColumn("peaceScoremedian", col("peaceScoresum") / col("numbercitizens"))
	//avrotestpeace = avrotestpeace.withColumn("Day", to_date(col("timestamp")))
	avrotestpeace.repartition(1).write.option("header",true).csv("scoreperdrone")

	bw.write("4) To see the mean of peacescores retrieved by each drone, see directory messageperdrone, which contains a csv file detailed")
	bw.newLine()


	//avrotestdronegroup2.show()
	//new FileWriter("renvoi.txt")

	bw.close()
	//println("FIN !")
}
