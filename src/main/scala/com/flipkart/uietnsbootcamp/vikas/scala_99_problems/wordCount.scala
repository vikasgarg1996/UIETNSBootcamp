package com.flipkart.uietnsbootcamp.vikas.scala_99_problems

/**
  * Created by vikas.garg on 27/12/16.
  */
import org.apache.spark._
object wordCount {
  val conf = new SparkConf().setAppName("myapp").setMaster("local")
  val sc = new SparkContext(conf)


  def main(args:Array[String]) = {
    println("Inside")
    val textFile = sc.textFile("/Users/vikas.garg/flipkart/scala_exercises/scala_problems.txt")
    val counts = textFile.flatMap(line => line.split(" "))
      .map(word => (word, 1))
      .reduceByKey(_ + _).collect
    counts.foreach(println)

    sc.parallelize(counts).saveAsTextFile("/Users/vikas.garg/flipkart/scala_exercises_new/")
  }
}