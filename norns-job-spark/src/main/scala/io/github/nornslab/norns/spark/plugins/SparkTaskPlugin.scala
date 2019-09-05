package io.github.nornslab.norns.spark.plugins

import com.typesafe.config.Config
import io.github.nornslab.norns.core.{Filter, Input, Output, TaskPlugin}
import io.github.nornslab.norns.spark.SJC
import org.apache.spark.sql.{Dataset, Row}

/**
  * @author Li.Wei by 2019/9/5
  */
abstract class SparkTaskPlugin(implicit val tc: (SJC, Config))
  extends TaskPlugin {

  override type C = SJC

  override def context: C = tc._1
}

abstract class SparkInput(implicit override val tc: (SJC, Config))
  extends SparkTaskPlugin with Input[Dataset[Row]]

abstract class SparkFilter(implicit override val tc: (SJC, Config))
  extends SparkTaskPlugin with Filter[Dataset[Row]]

abstract class SparkOutput(implicit override val tc: (SJC, Config))
  extends SparkTaskPlugin with Output[Dataset[Row]]
