package io.github.nornslab.norns.spark.plugins.output

import com.typesafe.config.Config
import io.github.nornslab.norns.core.ConfigKey
import io.github.nornslab.norns.spark.SJC
import io.github.nornslab.norns.spark.plugins.SparkOutput
import io.github.nornslab.norns.spark.plugins.output.StdoutConfigKeys.limitKey
import org.apache.spark.sql.{Dataset, Row}

/**
  * @author Li.Wei by 2019/9/5
  */
class Stdout(private implicit val _pluginInitConfig: Config,
             private implicit val _tc: (SJC, Config))
  extends SparkOutput {
  override def supportConfig: Seq[ConfigKey] = Seq(limitKey)

  override def output(d: Dataset[Row]): Unit = {
    val limit = pluginConfig.getInt(limitKey.key)

    d.collect().take(limit).foreach(println(_))
  }
}

/* ------------------------------------------------------------------------------------- *
   Stdout 插件支持配置项
 * ------------------------------------------------------------------------------------- */
private object StdoutConfigKeys {

  val limitKey = ConfigKey(key = "limit", default = Some(10), description = "limit N println")

}