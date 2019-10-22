package io.github.nornslab.norns.spark

import io.github.nornslab.norns.core.api.JobContext
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 封装 spark-job依赖 spark 相关对象信息
  *
  * @param sparkConfInit 扩展 sparkConf 配置信息
  */
class SparkJobContext(val sparkConfInit: SparkConf = new SparkConf()) extends JobContext {

  private val _sparkConf: SparkConf = buildSparkConf()
  private val _sc: SparkContext = SparkContext.getOrCreate(_sparkConf)
  private val _sSession: SparkSession = SparkSession.builder.config(_sparkConf).enableHiveSupport.getOrCreate

  /**
    * sparkConf 加载顺序
    * norns.spark-default -> spark -> sparkConfSetting
    *
    * @return SparkConf
    */
  def buildSparkConf(): SparkConf = {
    val stringToString = config.getOptional[Map[String, String]]("spark").getOrElse(Map.empty)
    sparkConfInit
      .setAll(config.getOptional[Map[String, String]]("spark-default").getOrElse(Map.empty))
      .setAll(stringToString)
  }


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