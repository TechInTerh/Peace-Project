package com.peace

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}
import io.minio.MinioClient
import org.apache.commons.io.IOUtils

import java.util.Base64
import scala.math.Ordered.orderingToOrdered
//Connect to S3


object Main extends App {
	val minioClient = new MinioClient("http://localhost:9000", "minioadmin", "minioadmin")

	val bucketName = "kafka-bucket"
	println(minioClient.bucketExists("kafka-bucket"))
	val list_obj = minioClient.listObjects(bucketName)
	list_obj.forEach(obj => {

		println(obj.get().objectName)
		val content = minioClient.getObject(bucketName, obj.get().objectName)
		val str_content = IOUtils.toString(content)
		println(str_content)
	})
	/*val obj = minioClient.listObjects(bucketName).forEach()
	println(minioClient.getBucketPolicy(bucketName))
	val stream = minioClient.getObject(bucketName,"object-name")
	val buf = new Array[Byte](16384)
	var bytesRead = 0
	while ( {
		(bytesRead = stream.read(buf, 0, buf.length)) >= 0
	}) System.out.println(new String(buf, 0, bytesRead))
	*/

}
