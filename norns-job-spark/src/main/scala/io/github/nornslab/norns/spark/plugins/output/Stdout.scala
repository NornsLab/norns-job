package io.github.nornslab.norns.spark.plugins.output

import io.github.nornslab.norns.core.api.{ConfigEntry, ConfigEntryBuilder, Configuration, TaskContext}
import io.github.nornslab.norns.core.plugins.BaseOutput
import io.github.nornslab.norns.spark.SJC
import io.github.nornslab.norns.spark.plugins.output.StdoutPluginConfigSpec.limitConfigSpec
import org.apache.spark.sql.{Dataset, Row}

/**
  * @author Li.Wei by 2019/9/5
  */
class Stdout(implicit override val pluginConfig: Configuration,
             implicit override val jc: SJC,
             implicit override val tc: TaskContext)
  extends BaseOutput[SJC, Dataset[Row]] {

  val limit = pluginConfig.get(limitConfigSpec)

  override def output(d: Dataset[Row]): Unit = {
    // scalastyle:off println
    d.collect().take(limit.intValue()).foreach(println(_))
    // scalastyle:on println
  }

  override def configSchema: Seq[ConfigEntry[_]] = Seq(limitConfigSpec)
}

/* ------------------------------------------------------------------------------------- *
   Stdout 插件支持配置项
 * ------------------------------------------------------------------------------------- */
object StdoutPluginConfigSpec {

  val limitConfigSpec = ConfigEntryBuilder("limit").intConf.checkValue(_ > 0, "limit must be > 0").create(Some(20))
}