import java.util.Properties
import org.apache.avro.reflect.AvroSchema
import org.apache.hadoop.conf.Configuration
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
import org.apache.hadoop.fs.{FileSystem, Path}

import registry.SchemaRegistry._

object Main extends App {
  def add_File(sc: SparkContext, file_path: String, file_name: String): Unit = {
    println("Adding file: " + file_path)
    val data = sc.textFile(file_path)
    data.saveAsTextFile("s3a://spark-test/" + file_name)
  }

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

  spark.read.option("header", "true").csv("s3a://spark-test/test.csv").show()
  spark.read.option("header", "true").csv("s3a://spark-test/*.csv").show()

  //val avrotest = spark.read.format("avro").load("drone-report00000000000.avro")

  //spark.read.format("avro").load("s3a://spark-test/drone-report00000000000.avro").show()
}
