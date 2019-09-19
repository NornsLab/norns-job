package io.github.nornslab.norns.spark

import io.github.nornslab.norns.core.api.Job
import org.apache.spark.sql.DataFrame

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



