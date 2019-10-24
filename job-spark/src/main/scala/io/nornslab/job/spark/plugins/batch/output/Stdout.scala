package io.nornslab.job.spark.plugins.batch.output

import io.nornslab.job.core.api.{ConfigEntry, ConfigEntryBuilder, NornsConfig, TaskContext}
import io.nornslab.job.core.plugins.BaseOutput
import io.nornslab.job.spark.SJC
import io.nornslab.job.spark.plugins.batch.output.Stdout.limitConfigEntry
import org.apache.spark.sql.{Dataset, Row}

/**
  * @author Li.Wei by 2019/9/5
  */
class Stdout(implicit override val pluginConfig: NornsConfig,
             implicit override val jc: SJC,
             implicit override val tc: TaskContext)
  extends BaseOutput[SJC, Dataset[Row]] {

  val limit = limitConfigEntry.readFrom(pluginConfig)

  override def output(d: Dataset[Row]): Unit = {
    // scalastyle:off println
    d.collect().take(limit.intValue()).foreach(println(_))
    // scalastyle:on println
  }

  override def configSchema: Seq[ConfigEntry[_]] = Seq(limitConfigEntry)
}

/* ------------------------------------------------------------------------------------- *
   Stdout 插件支持配置项
 * ------------------------------------------------------------------------------------- */
private object Stdout {

  val limitConfigEntry = ConfigEntryBuilder("limit").intConf.checkValue(_ > 0, "limit must be > 0").create(Some(20))
}