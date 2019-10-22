package io.github.nornslab.norns.spark

import io.github.nornslab.norns.core.api.Job
import org.apache.spark.sql.DataFrame

/**
  * @author Li.Wei by 2019/8/31
  */
trait SparkJob extends Job[SJC] {

  private val _jc: SJC = new SJC()

  implicit override def context: SJC = _jc

  def sql(sql: String)(implicit a: SJC): DataFrame = a.sparkSession.sql(sql)
}



