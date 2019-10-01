package io.github.nornslab.norns.spark.plugins.filter

import io.github.nornslab.norns.core.api.{Configuration, TaskContext}
import io.github.nornslab.norns.core.plugins.BaseFilter
import io.github.nornslab.norns.spark.SJC
import org.apache.spark.sql.{Dataset, Row}

/**
  * @author Li.Wei by 2019/9/5
  */
class Add(implicit override val pluginConfig: Configuration,
          implicit override val jc: SJC,
          implicit override val tc: TaskContext)
  extends BaseFilter[SJC, Dataset[Row]] {

  override def filter(d: Dataset[Row]): Dataset[Row] = d
}
