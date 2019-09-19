package io.github.nornslab.norns.spark.plugins.output

import io.github.nornslab.norns.core.api.{Configuration, PluginConfigSpec}
import io.github.nornslab.norns.core.plugins.BaseOutput
import io.github.nornslab.norns.spark.SJC
import org.apache.spark.sql.{Dataset, Row}

/**
  * @author Li.Wei by 2019/9/5
  */
class Stdout(override val pluginConfig: Configuration,
             override val context: SJC,
             override val data: Map[String, AnyRef])
  extends BaseOutput[Dataset[Row]](pluginConfig, context, data) {
  val limit = pluginConfig.get(StdoutPluginConfigSpec.limitConfigSpec).intValue()

  override def output(d: Dataset[Row]): Unit = {
    d.collect().take(limit).foreach(println(_))
  }
}

/* ------------------------------------------------------------------------------------- *
   Stdout 插件支持配置项
 * ------------------------------------------------------------------------------------- */
object StdoutPluginConfigSpec {

  val limitConfigSpec = PluginConfigSpec.number("limit", 10)
}