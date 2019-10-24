package io.nornslab.job.spark.plugins.streaming.input

import io.nornslab.job.core.api.{ConfigEntry, ConfigEntryBuilder, NornsConfig, TaskContext}
import io.nornslab.job.core.plugins.BaseInput
import io.nornslab.job.spark.SJC
import io.nornslab.job.spark.plugins.streaming.input.StructuredStreaming._
import org.apache.spark.sql.{Dataset, Row}

/** 官方文档 http://spark.apache.org/docs/2.4.4/structured-streaming-programming-guide.html#input-sources
  *
  * @author Li.Wei by 2019/10/23
  */
class StructuredStreaming(implicit override val pluginConfig: NornsConfig,
                          implicit override val jc: SJC,
                          implicit override val tc: TaskContext)
  extends BaseInput[SJC, Dataset[Row]] {

  val format = formatConfig.readFrom(pluginConfig)
  val schema = schemaConfig.readFrom(pluginConfig)
  val options = optionsConfig.readFrom(pluginConfig)

  override def configSchema: Seq[ConfigEntry[_]] = Seq(schemaConfig, formatConfig, optionsConfig)

  override def input: Dataset[Row] = {
    val reader = jc.sparkSession
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

private object StructuredStreaming {

  val schemaConfig = ConfigEntryBuilder("schema")
    .doc("use [[org.apache.spark.sql.streaming.DataStreamReader]] api")
    .stringConf.create(Some(""))

  val formatConfig = ConfigEntryBuilder("format")
    .doc("use [[org.apache.spark.sql.streaming.DataStreamReader]] api," +
      " see http://spark.apache.org/docs/latest/structured-streaming-programming-guide.html#input-sources")
    .stringConf.create()

  val optionsConfig = ConfigEntryBuilder("options")
    .doc("use [[org.apache.spark.sql.streaming.DataStreamReader]] api")
    .stringMapConf.create(Some(Map.empty))

}