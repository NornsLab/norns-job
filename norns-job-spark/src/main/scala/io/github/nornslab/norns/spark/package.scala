package io.github.nornslab.norns

import io.github.nornslab.norns.core.{Filter, Input, Output}
import org.apache.spark.sql.{Dataset, Row}

/**
  * @author Li.Wei by 2019/9/3
  */
package object spark {

  type SJC = SparkJobContext
  // type STC = SparkTaskContext

  type SpInput = Input[SJC, Dataset[Row]]
  type SpFilter = Filter[SJC, Dataset[Row]]
  type SpOutput = Output[SJC, Dataset[Row]]

}
