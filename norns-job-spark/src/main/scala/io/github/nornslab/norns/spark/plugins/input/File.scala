package io.github.nornslab.norns.spark.plugins.input

import com.typesafe.config.Config
import io.github.nornslab.norns.spark.SJC
import io.github.nornslab.norns.spark.plugins.SparkInput
import org.apache.spark.sql.{Dataset, Row}

/**
  * @author Li.Wei by 2019/9/5
  */
class File(implicit override val tc: (SJC, Config)) extends SparkInput {

  override def init: Either[Throwable, this.type] = {
    Right(this)
  }

  override def input(): Dataset[Row] = {
    val frame = context.sparkSession.read.text("file:///Users/liwei/Development/workspace/intellij/norns-job/" +
      "norns-job-examples/files/text.input.txt")
    // frame.collect().foreach(println(_))
    frame
  }

}
