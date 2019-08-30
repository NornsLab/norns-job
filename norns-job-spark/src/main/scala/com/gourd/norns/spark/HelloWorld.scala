package com.gourd.norns.spark

import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author Li.Wei by 2019/8/30
  */
object HelloWorld {

  def main(args: Array[String]): Unit = { // Local mode
    val sparkConf = new SparkConf().setAppName("HelloWorld")
      .setMaster("local[1]")
    val ctx = new SparkContext(sparkConf)
    ctx.setLogLevel("INFO")
    ctx.parallelize(Seq("A1", "B2", "C3", "D4", "F5")).foreach(println(_))
    ctx.stop()
  }

}
