package com.flipkart.uietnsbootcamp.shashi

/**
  * Created by shashi.kushwaha on 27/12/16.
  */

import org.apache.spark._
object PiThingy {
  val conf = new SparkConf().setAppName("myApp").setMaster("local[2]")
  val sc = new SparkContext(conf)
  def main(args: Array[String]) = {
    val file = sc.textFile("/Users/shashi.kushwaha/flipkart/Hello/src/main/scala-2.11/sparkOne/Second.scala ")
    val words = file.map(line => line.split(" ")).map(word => (word, 1))
    val counts = words.reduceByKey(_+_);
    counts.foreach(println);
  }

}
