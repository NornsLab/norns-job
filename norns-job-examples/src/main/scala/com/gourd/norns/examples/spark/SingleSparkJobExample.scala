package com.gourd.norns.examples.spark

import com.gourd.norns.core.NornsMain
import com.gourd.norns.spark.SparkJob

/**
  * @author Li.Wei by 2019/8/30
  */
object SingleSparkJobExample {
  def main(args: Array[String]): Unit = NornsMain.work(classOf[SingleSparkJobExample])
}

class SingleSparkJobExample extends SparkJob {

  override def run(): Unit = {
    sql("select * from db")
  }

}
