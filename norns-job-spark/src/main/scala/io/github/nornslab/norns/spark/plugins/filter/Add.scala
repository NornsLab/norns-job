package io.github.nornslab.norns.spark.plugins.filter

import com.typesafe.config.Config
import io.github.nornslab.norns.spark.SJC
import io.github.nornslab.norns.spark.plugins.SparkFilter
import org.apache.spark.sql.{Dataset, Row}

/**
  * @author Li.Wei by 2019/9/5
  */
class Add(implicit override val tc: (SJC, Config)) extends SparkFilter {

  override def filter(d: Dataset[Row]): Dataset[Row] = d
}
