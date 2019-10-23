package io.github.nornslab.norns.spark.plugins.input

import io.github.nornslab.norns.core.api.{ConfigEntry, Configuration, TaskContext}
import io.github.nornslab.norns.core.plugins.BaseInput
import io.github.nornslab.norns.spark.SJC
import io.github.nornslab.norns.spark.plugins.PluginConfigEntry.{formatConfig, optionsConfig, schemaConfig}
import org.apache.spark.sql.{Dataset, Row}

/** 官方文档 http://spark.apache.org/docs/2.4.4/structured-streaming-programming-guide.html#input-sources
  *
  * @author Li.Wei by 2019/10/23
  */
class StructuredStreamingInput(implicit override val pluginConfig: Configuration,
                               implicit override val jc: SJC,
                               implicit override val tc: TaskContext)
  extends BaseInput[SJC, Dataset[Row]] {

  val format = pluginConfig.get(formatConfig)
  val schema = pluginConfig.get(schemaConfig)
  val options = pluginConfig.get(optionsConfig)

  override def configSchema: Seq[ConfigEntry[_]] = Seq(schemaConfig, formatConfig, optionsConfig)

  override def input: Dataset[Row] = {
    val reader =
      jc.sparkSession
        .readStream
        .format(format)
        .options(options)

    schema match {
      case "" => logger.info("empty schema , don`t use userSpecifiedSchema")
      case _ => reader.schema(schema)
    }

    reader.load()
  }
}
