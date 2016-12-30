package com.flipkart.uietnsbootcamp.vikas.mapReduce

import org.apache.spark.{SparkConf, SparkContext}

import scala.io.Source

/**
  * Created by vikas.garg on 30/12/16.
  */
object matrixMatrix1 {
  def main(args: Array[String]): Unit = {
    val matrixFile1 = "./src/main/resources/vikas_matrix1.txt"
    val matrixFile2 = "./src/main/resources/vikas_matrix1.txt"

    val storing_matrix = Source.fromFile(matrixFile1).getLines().toList.map(line=>{
      val singleLine = line.split(",")
      (singleLine(0).toLong,singleLine(1).toLong,singleLine(2).toDouble)})

    val sparkConf = new SparkConf ().setAppName ("Matrix Vector Multiplication").setMaster("local")
    val sc = new SparkContext (sparkConf)

    val globalMatrix = sc.broadcast(storing_matrix)

    val matrix = sc.textFile(matrixFile2).map(line=>{
      val singleLine = line.split(",")
      (singleLine(0).toLong,singleLine(1).toLong,singleLine(2).toDouble)
    })

    val finalAnswer = matrix.flatMap(first =>{
      globalMatrix.value.filter(element => first._2 ==element._1).map(second =>{
        ((first._1, second._2), first._3*second._3)
      })
    }).reduceByKey(_+_).collect()


    println(finalAnswer.toList)
  }
}
