package io.github.nornslab.norns.spark

import com.typesafe.config.Config
import io.github.nornslab.norns.core._
import org.apache.spark.sql.{Dataset, Row}

/**
  * @author Li.Wei by 2019/9/3
  */
class SparkTask(implicit override val tc: (SJC, Config))
  extends BaseTask[SJC] {

  override def start(): Unit = {} // 默认实现
}


abstract class SparkPluginTask(override implicit val tc: (SJC, Config))
  extends BasePluginTask[SJC, Dataset[Row]]




