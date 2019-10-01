package io.github.nornslab.norns.spark.plugins.output

import io.github.nornslab.norns.core.api.{Configuration, PluginConfigSpec, TaskContext}
import io.github.nornslab.norns.core.plugins.BaseOutput
import io.github.nornslab.norns.spark.SJC
import org.apache.spark.sql.{Dataset, Row}

/**
  * @author Li.Wei by 2019/9/5
  */
class Stdout(implicit override val pluginConfig: Configuration,
             implicit override val jc: SJC,
             implicit override val tc: TaskContext)
  extends BaseOutput[SJC, Dataset[Row]] {

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