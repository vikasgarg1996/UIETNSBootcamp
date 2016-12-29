package com.flipkart.uietnsbootcamp.prashant

/**
  * Created by prashant.s on 27/12/16.
  */
class Scala99Problems {

  def removeAt(pos:Int, list:List[Any]) = {
    if(list.length == 0 || pos > list.length) (Nil, Nil)
    else{
      val element = list(pos)
      val splitList = list.splitAt(pos);
      val newList = splitList._1 ++ splitList._2.drop(1)
      (newList, element)
    }
  }
  def main(args: Array[String]): Unit = {
   removeAt(2,List(1,2,3,5))
  }

}
