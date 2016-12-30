package com.flipkart.uietnsbootcamp.vikas.mapReduce

import org.apache.spark.{SparkConf, SparkContext}
import scala.io.Source
/**
  * Created by vikas.garg on 29/12/16.
  */
object matrixVector1 {
  def main(args: Array[String]): Unit = {
    val matrixFile = "./src/main/resources/vikas_matrix1.txt"
    val vectorFile = "./src/main/resources/vikas_vector1.txt"

    val values = Source.fromFile(vectorFile).getLines().toList
    val vector = values.map(line=>{
      val sp = line.split(",")
      (sp(0).toLong,sp(1).toDouble)}).toMap
 //       .apply(1).toDouble
 //   }).zipWithIndex.map(entry =>(entry._2.toLong,entry._1)).toMap

    val sparkConf = new SparkConf ().setAppName ("Matrix Vector Multiplication").setMaster("local")
    val sc = new SparkContext (sparkConf)
    val globalVector = sc.broadcast(vector)
    val matrix = sc.textFile(matrixFile).map(line=>{
      val singleLine = line.split(",")
      (singleLine(0).toLong,singleLine(1).toLong,singleLine(2).toDouble)
    })

    val mapMatrix =  matrix.map(tuple => {
      (tuple._1,tuple._3*globalVector.value.get(tuple._2).getOrElse(0.0))
    })

    val finalAnswer = mapMatrix.reduceByKey(_+_).collect().toList
    println(finalAnswer)

    println(values)
    println(matrix.collect().toList)
  }
}
