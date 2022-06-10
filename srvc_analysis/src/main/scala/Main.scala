import java.util.Properties
import org.apache.avro.reflect.AvroSchema
import org.apache.hadoop.conf.Configuration
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.hadoop.fs.{FileSystem, Path}

object Main extends App {
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
	avrotest.show()

	//Nombre de messages de drones
	println(avrotest.count())

	//Groupement par Timestamp
	val avrotesttimestampgroup = avrotest.groupBy("timestamp").count().sort(desc("count"))
	avrotesttimestampgroup.show()

	println(avrotesttimestampgroup.count())

	//Groupement par drone
	val avrotestdronegroup = avrotest.groupBy("droneId").count().sort(desc("count"))
	avrotestdronegroup.show()

	println(avrotestdronegroup.count())

	//Mes tests pour obtenir le peacescore
	avrotest.withColumn("citizensstring", array_join(avrotest.col("words"),"|")).show()

	val avrotestexploded = avrotest.withColumn("citizen", explode(col("citizens")))

	avrotestexploded.withColumn("peacescore",col("citizen.peaceScore")).show()

	//avrotest.withColumn("partofpeacescore", col("citizens").getItem(0)).printSchema()

	//val avrotestdronegroup2 = avrotest.groupBy("droneId").sum("citizens.peaceScore")

	avrotest.printSchema()

	val avrotestpeace = avrotest.withColumn("peacescores", col("citizens.peaceScore"))

	avrotestpeace.show()

	avrotestpeace.withColumn("sumscore", aggregate(col("peacescores"), lit(0), (x, y) => (x + y))).show()

	//avrotestdronegroup2.show()

	println("FIN !")
}
