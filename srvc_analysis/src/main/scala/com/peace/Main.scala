package com.peace

import org.apache.spark.sql._
import org.apache.spark.sql.types._

object Main extends App {
	val spark = SparkSession.builder().appName("Peace-Analyzer")
		.master("local[4]")
		.getOrCreate()
	val sc = spark.sparkContext
	sc.hadoopConfiguration.set("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
	sc.hadoopConfiguration.set("fs.s3a.endpoint", "http://127.0.0.1:9000")
	sc.hadoopConfiguration.set("fs.s3a.access.key", "minioadmin")
	sc.hadoopConfiguration.set("fs.s3a.secret.key", "minioadmin")
	sc.hadoopConfiguration.set("fs.s3a.path.style.access", "true")
	val df = sc.textFile("s3a://spark-test/")
	df.foreach(println)
}
