package io.github.nornslab.norns.spark

import io.github.nornslab.norns.core.api.base.BaseTaskJob
import org.apache.spark.sql.{Dataset, Row}

/**
  * @author Li.Wei by 2019/9/3
  */
class SparkTaskJob extends BaseTaskJob[SJC, Dataset[Row]] with SparkJob