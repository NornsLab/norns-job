package io.github.nornslab.norns.spark

import io.github.nornslab.norns.core.{Constant, Job, JobContext}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.JavaConverters._

/**
  * @author Li.Wei by 2019/8/31
  */
trait SparkJob extends Job {

  override type JC = SparkJobContext

  private lazy val _jc: JC = new JC(
    Map(
      "spark.app.name" -> this.getClass.getName,
      "spark.master" -> "local"
    )
  )

  implicit override def jc: JC = _jc

  def sql(sql: String)(implicit a: JC): DataFrame = a.sparkSession.sql(sql)
}

class SparkJobContext(val sparkConfSetting: Traversable[(String, String)] = Map.empty) extends JobContext {

  private val _sparkConf: SparkConf = buildSparkConf()
  private val _sc: SparkContext = SparkContext.getOrCreate(_sparkConf)
  private val _sSession: SparkSession = SparkSession.builder.config(_sparkConf).enableHiveSupport.getOrCreate

  def buildSparkConf(): SparkConf = new SparkConf()
    .setAll(Constant.loadConfFile(Some(config) -> "norns.spark-default.conf").entrySet().asScala
      .map(f => f.getKey -> f.getValue.toString).toMap
    )
    .setAll(config.withOnlyPath("norns.spark").entrySet().asScala.map(f => f.getKey -> f.getValue.toString).toMap)
    .setAll(sparkConfSetting)

  implicit def sparkContext: SparkContext = _sc

  implicit def sparkSession: SparkSession = _sSession

  override def close(): Unit = {
    super.close()
    _sSession.stop()
    _sc.stop()
  }
}
