package com.peace

import org.apache.spark.SparkContext
import org.apache.spark.sql._
import org.apache.spark.sql.types._

object Main extends App {
	def add_File(sc: SparkContext, file_path: String,file_name: String): Unit = {
		println("Adding file: " + file_path)
		val data = sc.textFile(file_path)
		data.saveAsTextFile("s3a://spark-test/"+file_name)
	}
	val spark = SparkSession.builder().appName("Peace-Analyzer")
		.master("local[4]")
		.getOrCreate()
	val sc = spark.sparkContext
	sc.hadoopConfiguration.set("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
	sc.hadoopConfiguration.set("fs.s3a.endpoint", "http://127.0.0.1:9000")
	sc.hadoopConfiguration.set("fs.s3a.access.key", "minioadmin")
	sc.hadoopConfiguration.set("fs.s3a.secret.key", "minioadmin")
	sc.hadoopConfiguration.set("fs.s3a.path.style.access", "true")
	sc.hadoopConfiguration.set("fs.s3a.multipart.size", "10485760")
	sc.hadoopConfiguration.set("fs.s3a.fast.upload", "true")
	add_File(sc,"example_drone_msg/example1.csv","example1.csv")
	println("Reading file: csv")
	val df = sc.textFile("s3a://spark-test/")
	df.foreach(println)

}
