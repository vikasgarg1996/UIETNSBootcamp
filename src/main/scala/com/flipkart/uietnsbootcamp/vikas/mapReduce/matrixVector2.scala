package com.flipkart.uietnsbootcamp.vikas.mapReduce

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by vikas.garg on 29/12/16.
  */
object matrixVector2 {
  def main(args: Array[String]): Unit ={
    val matrixFile = "./src/main/resources/vikas_matrix1.txt"
    val vectorFile = "./src/main/resources/vikas_vector1.txt"

    val sparkConf = new SparkConf ().setAppName ("Matrix Vector Multiplication").setMaster("local")
    val sc = new SparkContext (sparkConf)

    val matrix = sc.textFile(matrixFile).map(line=>{
      val singleLine = line.split(",")
      (singleLine(1).toLong,(singleLine(0).toLong,singleLine(2).toDouble))
    })

    val vector = sc.textFile(vectorFile).map(line=>{
      val singlLine = line.split(",")
      (singlLine(0).toLong,singlLine(1).toDouble)
    })

    val mapValues = matrix.join(vector).map(entry=>(entry._2._1._1,entry._2._1._2*entry._2._2)).reduceByKey(_+_)
    println(mapValues.collect().toList)

  }

}
