package com.flipkart.uietnsbootcamp.ayan

/**
  * Created by ayan.ghatak on 26/12/16.
  */
object P01 {
  def main(args: Array[String]) {
    println(last(List(2,3,4,5)))
    println(last(List("as","b","c")))
    println(last(List()))
  }
  def last[A](list: List[A]): A ={
    if(list.isEmpty) throw new NoSuchElementException
    else list.last
  }
}
