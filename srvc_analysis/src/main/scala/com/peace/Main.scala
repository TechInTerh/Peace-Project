package com.peace

import org.apache.spark.SparkContext
import org.apache.spark.sql._
import org.apache.spark.sql.types._
import com.sksamuel.avro4s
import com.sksamuel.avro4s.AvroInputStream
import org.apache.avro.reflect.AvroSchema

import scala.reflect.io.File
import model.DroneReportModel._

object Main extends App {
	def add_File(sc: SparkContext, file_path: String,file_name: String): Unit = {
		println("Adding file: " + file_path)
		val data = sc.textFile(file_path)
		data.saveAsTextFile("s3a://spark-test/" + file_name)
	}
	val spark = SparkSession.builder().appName("Peace-Analyzer")
		.master("local[4]")
		.config("fs.s3a.access.key", "minioadmin")
		.config("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
		.config("fs.s3a.endpoint", "http://127.0.0.1:9000")
		.config("fs.s3a.access.key", "minioadmin")
		.config("fs.s3a.secret.key", "minioadmin")
		.config("fs.s3a.path.style.access", "true")
		.config("fs.s3a.multipart.size", "10485760")
		.config("fs.s3a.fast.upload", "true")
		.getOrCreate()

	spark.read.option("header", "true").csv("s3a://spark-test/test.csv").show()

	def getListOfFiles():List[File] = {
		val d = new File("s3a://spark-test/")
		if (d.exists && d.isDirectory) {
			d.listFiles.filter(_.isFile).toList
		} else {
			List[File]()
		}
	}

	val schema = AvroSchema[DroneReportModel]
	val listoffilesbucket = getListOfFiles()
	for (fileavro <- listoffilesbucket)
		{
			val is = AvroInputStream.data[DroneReportModel].from(fileavro).build(schema)
			val dronereport = is.iterator.toSet
			is.close()

			println(dronereport.mkString("\n"))
		}
}
