package com.flipkart.uietnsbootcamp.vikas.mapReduce

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by vikas.garg on 30/12/16.
  */
object matrixMatrix2 {
  def main(args: Array[String]): Unit = {
    val matrixFile1 = "./src/main/resources/vikas_matrix1.txt"
    val matrixFile2 = "./src/main/resources/vikas_matrix1.txt"

    val sparkConf = new SparkConf().setAppName("Matrix Vector Multiplication").setMaster("local")
    val sc = new SparkContext(sparkConf)

    val first_matrix = sc.textFile(matrixFile1).map(line => {
      val singleLine = line.split(",")
      (singleLine(1).toLong, (singleLine(0).toLong, singleLine(2).toDouble))
    })

    val second_matrix = sc.textFile(matrixFile2).map(line => {
      val singleLine = line.split(",")
      (singleLine(0).toLong, (singleLine(1).toLong, singleLine(2).toDouble))
    })

    val finalAnswer = first_matrix.join(second_matrix).map(entry=>{
      ((entry._2._1._1,entry._2._2._1), entry._2._2._2 * entry._2._1._2)
    }).reduceByKey(_+_)

    println(finalAnswer.collect().toList)
  }
}
