package io.github.nornslab.norns.spark

import io.github.nornslab.norns.core.api.{Job, JobContext}
import io.github.nornslab.norns.core.utils.ConfigUtils
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.JavaConverters._

/**
  * @author Li.Wei by 2019/8/31
  */
trait SparkJob extends Job {

  override type C = SJC

  private val _jc: C = new C(
    Map(
      "spark.app.name" -> name,
      "spark.master" -> "local"
    )
  )

  override def context: C = _jc

  def sql(sql: String)(implicit a: C): DataFrame = a.sparkSession.sql(sql)
}

/**
  * 封装 spark-job依赖 spark 相关对象信息
  *
  * @param sparkConfSetting 扩展sparkConf配置信息
  */
class SparkJobContext(val sparkConfSetting: Traversable[(String, String)] = Map.empty) extends JobContext {

  private val _sparkConf: SparkConf = buildSparkConf()
  private val _sc: SparkContext = SparkContext.getOrCreate(_sparkConf)
  private val _sSession: SparkSession = SparkSession.builder.config(_sparkConf).enableHiveSupport.getOrCreate

  /**
    * sparkConf 加载顺序
    * norns.spark-default.conf -> JobContext.config 中 key=spark 配置信息 -> sparkConfSetting
    *
    * @return SparkConf
    */
  def buildSparkConf(): SparkConf = new SparkConf()
    .setAll(ConfigUtils.loadConfFile(Some(config) -> "norns.spark-default.conf").entrySet().asScala
      .map(f => f.getKey -> f.getValue.toString).toMap
    )
    .setAll(config.withOnlyPath("norns.spark").entrySet().asScala.map(f => f.getKey -> f.getValue.toString).toMap)
    .setAll(sparkConfSetting)

  def sparkContext: SparkContext = _sc

  def sparkSession: SparkSession = _sSession

  override def close(): Unit = {
    super.close()
    sparkSession.stop()
    sparkContext.stop()
  }
}

object SparkJobContext {

  // 与 SparkJobContext 相关常用方法定义 。 e.g sql 、 df ...

  def sql(sqlStr: String)(implicit sjc: SJC): DataFrame = sjc.sparkSession.sql(sqlStr)

}