package io.github.nornslab.norns.spark

import io.github.nornslab.norns.core.api.Job
import org.apache.spark.sql.DataFrame

/**
  * @author Li.Wei by 2019/8/31
  */
trait SparkJob extends Job {

  override type JC = SJC

  private val _jc: JC = new JC(
    Map(
      "spark.app.name" -> name,
      "spark.master" -> "local"
    )
  )

  implicit override def context: JC = _jc

  def sql(sql: String)(implicit a: JC): DataFrame = a.sparkSession.sql(sql)
}



