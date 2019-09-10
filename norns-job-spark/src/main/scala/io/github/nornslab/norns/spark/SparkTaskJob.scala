package io.github.nornslab.norns.spark

import io.github.nornslab.norns.core.BaseTaskJob
import org.apache.spark.sql.{Dataset, Row}

/**
  * @author Li.Wei by 2019/9/3
  */
class SparkTaskJob extends BaseTaskJob with SparkJob {

  override type PDT = Dataset[Row]

}