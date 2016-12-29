package com.flipkart.uietnsbootcamp.gaurav.Scala20Problems

/**
  * Created by vikas.garg on 27/12/16.
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

object Demo2 {

  def main(args: Array[String]): Unit= {

    val consumerKey = "LgLwP44CFFCTv3HQpzKsmlGU2"
    val consumerSecret = "vrb4S9hnxGfIKl41Cbe9VJ5906hYZgKxBwigeL3C0pZriRTauP"
    val accessToken = "1642873482-GjdsvfgRXhHfW5jyjd3CFFOrzyCwzI1qB05FJLT"
    val accessTokenSecret = "zY6BV5a0eDOHmz5bxQcjESwV900XWO6Z34LxyhRwySWwW"
    val url = "https://stream.twitter.com/1.1/statuses/filter.json"

    val sparkConf = new SparkConf ().setAppName ("Twitter Streaming").setMaster("local")
    val sc = new SparkContext (sparkConf)



    // Twitter Streaming
    val ssc = new JavaStreamingContext (sc, Seconds (2) )

    val conf = new ConfigurationBuilder ()
    conf.setOAuthAccessToken (accessToken)
    conf.setOAuthAccessTokenSecret (accessTokenSecret)
    conf.setOAuthConsumerKey (consumerKey)
    conf.setOAuthConsumerSecret (consumerSecret)
    conf.setStreamBaseURL (url)
    conf.setSiteStreamBaseURL (url)

    val filter = Array ("Twitter", "Hadoop", "Big Data")

    val auth = AuthorizationFactory.getInstance (conf.build () )
    val tweets: JavaReceiverInputDStream[twitter4j.Status] = TwitterUtils.createStream (ssc, auth, filter)

    val statuses = tweets.dstream.map (status => status.getText)
    var i:Int = 0
    statuses.foreachRDD(data => {
      data.saveAsTextFile("/Users/vikas.garg/tweets/New_Twit_"+i)
      i += 1
    })

    statuses.print ()
    ssc.start ()
    ssc.awaitTermination()
  }
}
