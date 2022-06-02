import org.apache.spark.sql._
import org.apache.spark.sql.types._

object app {
	def main(args: Array[String]): Unit = {
		val schema = StructType(
			List(
				StructField("name", StringType, true),
				StructField("age", IntegerType, false)
			)
		)
		/*
		val spark = SparkSession.builder.appName("spark-select in python").master("local[*]").getOrCreate()
		val df = spark
			.read
			.format("minioSelectCSV")
			//enter crendentials
			.option("accessKey", "minioadmin")
			.option("secretKey", "minioadmin")
			.schema(schema)
			.load("s3://spark-test/test.csv")

		println(df.show())

		println(df.select("*").filter("age > 19").show())
		*/
		val spark = SparkSession.builder().appName("AliceProcessingTwentyDotTwo")
			.config("spark.serializer", "org.apache.spark.serializer.KryoSerializer").master("local[1]")
			.getOrCreate()
		val sc= spark.sparkContext
		sc.hadoopConfiguration.set("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
		sc.hadoopConfiguration.set("fs.s3a.endpoint", "http://127.0.0.1:9000")
		sc.hadoopConfiguration.set("fs.s3a.access.key", "minioadmin")
		sc.hadoopConfiguration.set("fs.s3a.secret.key", "minioadmin")
		sc.hadoopConfiguration.set("fs.s3a.path.style.access", "true")
		sc.hadoopConfiguration.set("fs.s3a.threads.max", "4")
		sc.hadoopConfiguration.set("fs.s3a.threads.core", "4")
		sc.hadoopConfiguration.set("fs.s3a.multipart.size", "10485760")
		sc.hadoopConfiguration.set("fs.s3a.multipart.threshold", "10485760")
		sc.hadoopConfiguration.set("fs.s3a.block.size", "33554432")
		sc.hadoopConfiguration.set("fs.s3a.connection.ssl.enabled","false")
		val df = sc.textFile("s3a://spark-test/")
		df.foreach(println)
	}
}
