package io.nornslab.job.spark

import io.nornslab.job.core.api.Job
import io.nornslab.job.core.api.base.{BaseTaskBuilder, BaseTaskJob}
import org.apache.spark.sql.{DataFrame, Dataset, Row}

/**
  * @author Li.Wei by 2019/8/31
  */
trait SparkJob extends Job[SJC] {

  private val _jc: SJC = new SJC()

  implicit override def context: SJC = _jc

  def sql(sql: String)(implicit a: SJC): DataFrame = a.sparkSession.sql(sql)
}


abstract class SparkTaskJob extends BaseTaskJob[SJC, Dataset[Row]] with SparkJob {

  // todo 该实例支持反射获取
  val taskBuilderImpl = new BaseTaskBuilder[SJC, Dataset[Row]]()

  override def taskBuilder: BaseTaskBuilder[SJC, Dataset[Row]] = taskBuilderImpl
}

/**
  * 支持 spark 批任务处理
  */
class SparkBatchTaskJob extends SparkTaskJob

/**
  * 支持 spark-streaming 流式任务处理
  */
class SparkStreamingTaskJob extends SparkTaskJob

/**
  * 支持 spark-structured-streaming 流式任务处理
  */
class SparkStructuredStreamingTaskJob extends SparkTaskJob {

  override def start(): Unit = {
    super.start()
    context.sparkSession.streams.awaitAnyTermination()
  }

}
