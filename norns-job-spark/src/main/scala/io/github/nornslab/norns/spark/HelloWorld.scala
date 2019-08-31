package io.github.nornslab.norns.spark

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
  * @author Li.Wei by 2019/8/30
  */
object HelloWorld {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
      .setAppName("HelloWorld")
      .setMaster("local[1]")
    val spark = SparkSession.builder.config(sparkConf).getOrCreate
    spark.sql("select * from db")
    spark.stop()
  }

}
