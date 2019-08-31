package io.github.nornslab.norns.examples.spark

import io.github.nornslab.norns.core.NornsMain
import io.github.nornslab.norns.spark.SparkJob

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
