package com.flipkart.uietnsbootcamp.gaurav.Scala20Problems

/**
  * Created by gaurav.malhotra on 27/12/16.
  */

import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.twitter._
import org.apache.spark.streaming.StreamingContext._
import twitter4j.auth.Authorization
import twitter4j.Status
import twitter4j.auth.AuthorizationFactory
import twitter4j.conf.ConfigurationBuilder
import org.apache.spark.streaming.api.java.JavaStreamingContext

import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.api.java.function.Function
import org.apache.spark.streaming.Duration
import org.apache.spark.streaming.api.java.JavaDStream
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream
import java.io._
import scala.io.Source

object Demo3 {

  def main(args: Array[String]): Unit= {

    val lines = Source.fromFile("./src/main/resources/twitter_vikas.txt").getLines().toList

    val allValues = lines.map(single=>{
      val sp = single.split("=")
      (sp.head,sp.tail.head)
    }).toMap

    val consumerKey = allValues("consumerKey")
    val consumerSecret = allValues("consumerSecret")
    val accessToken = allValues("accessToken")
    val accessTokenSecret = allValues("accessTokenSecret")
    val url = "https://stream.twitter.com/1.1/statuses/filter.json"

    val sparkConf = new SparkConf ().setAppName ("Twitter Streaming").setMaster("local")
    val sc = new SparkContext (sparkConf)



    // Twitter Streaming
    val ssc = new StreamingContext (sc, Seconds (5) )
    ssc.checkpoint("/Users/vikas.garg/flipkart/checkpoint/")

    val conf = new ConfigurationBuilder ()
    conf.setOAuthAccessToken (accessToken)
    conf.setOAuthAccessTokenSecret (accessTokenSecret)
    conf.setOAuthConsumerKey (consumerKey)
    conf.setOAuthConsumerSecret (consumerSecret)
    conf.setStreamBaseURL (url)
    conf.setSiteStreamBaseURL (url)

    val filter = Array ("Twitter", "Hadoop", "Big Data")

    val auth = AuthorizationFactory.getInstance (conf.build () )
    //val tweets = ssc.twitterStream()
    val tweets = TwitterUtils.createStream (ssc, Some(auth), filter)

    val statuses = tweets.map (status => status.getText)
    val words = statuses.flatMap(single => single.split(" "))
    val hashtags = words.filter(single => single.startsWith("#"))



    val counts = hashtags.map(tag => (tag, 1))
      .reduceByKeyAndWindow(_ + _, _ - _, Seconds(60 * 5), Seconds(5))

    val sorted = counts.map{ case(tag,count) => (count,tag)}.transform(rdd=>rdd.sortByKey(false))

    var j:Int = 0
    sorted.foreachRDD{rdd=> val wr = new FileWriter(new File("/Users/vikas.garg/tweets/Top_10_"+j))
      wr.write("\nTop 10 hashtags:\n" + rdd.take(10).mkString("\n"))
      wr.close()
      j += 1
    }

    var i:Int = 0
    counts.foreachRDD(data => {
      data.repartition(1).saveAsTextFile("/Users/vikas.garg/tweets/New_Twit_"+i)
      i += 1
    })

    counts.print ()
    ssc.start ()
    ssc.awaitTermination()
  }
}
