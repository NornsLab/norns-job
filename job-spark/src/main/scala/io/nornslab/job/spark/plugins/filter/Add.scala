package io.nornslab.job.spark.plugins.filter

import io.nornslab.job.core.api.{ConfigEntry, NornsConfig, TaskContext}
import io.nornslab.job.core.plugins.BaseProcessors
import io.nornslab.job.spark.SJC
import org.apache.spark.sql.{Dataset, Row}

/**
  * @author Li.Wei by 2019/9/5
  */
class Add(implicit override val pluginConfig: NornsConfig,
          implicit override val jc: SJC,
          implicit override val tc: TaskContext)
  extends BaseProcessors[SJC, Dataset[Row]] {

  override def process(d: Dataset[Row]): Dataset[Row] = d

  override def configSchema: Seq[ConfigEntry[_]] = Seq.empty
}
