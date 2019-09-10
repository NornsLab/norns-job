package io.github.nornslab.norns.spark.plugins.output

import com.typesafe.config.Config
import io.github.nornslab.norns.spark.SJC
import io.github.nornslab.norns.spark.plugins.SparkOutput
import org.apache.spark.sql.{Dataset, Row}

/**
  * @author Li.Wei by 2019/9/5
  */
class Stdout(private implicit val _pluginInitConfig: Config,
             private implicit val _tc: (SJC, Config))
  extends SparkOutput {
  override def output(d: Dataset[Row]): Unit = {
    d.collect().take(10).foreach(println(_))
  }
}
