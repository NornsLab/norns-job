package io.github.nornslab.norns.spark.plugins.filter

import io.github.nornslab.norns.core.api.Configuration
import io.github.nornslab.norns.core.plugins.BaseFilter
import io.github.nornslab.norns.spark.SJC
import org.apache.spark.sql.{Dataset, Row}

/**
  * @author Li.Wei by 2019/9/5
  */
class Add(override val pluginConfig: Configuration,
          override val context: SJC,
          override val data: Map[String, AnyRef])
  extends BaseFilter[Dataset[Row]](pluginConfig, context, data) {

  override def filter(d: Dataset[Row]): Dataset[Row] = d
}
