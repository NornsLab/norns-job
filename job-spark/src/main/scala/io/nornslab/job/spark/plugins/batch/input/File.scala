package io.nornslab.job.spark.plugins.batch.input

import io.nornslab.job.core.api.{ConfigEntry, ConfigEntryBuilder, NornsConfig, TaskContext}
import io.nornslab.job.core.plugins.input.BaseFile
import io.nornslab.job.spark.SJC
import io.nornslab.job.spark.plugins.batch.input.File._
import org.apache.spark.sql.{Dataset, Row}

/**
  * @author Li.Wei by 2019/9/5
  */
class File(implicit override val pluginConfig: NornsConfig,
           implicit override val jc: SJC,
           implicit override val tc: TaskContext)
  extends BaseFile[SJC, Dataset[Row]] {

  val path = s"""file://${pathConfig.readFrom(pluginConfig)}"""
  val schema = schemaConfig.readFrom(pluginConfig)
  val format = formatConfig.readFrom(pluginConfig)
  val options = optionsConfig.readFrom(pluginConfig)

  override def configSchema: Seq[ConfigEntry[_]] =
    Seq(pathConfig, schemaConfig, formatConfig, optionsConfig)

  override def input: Dataset[Row] = {
    val read = jc.sparkSession.read.options(options)

    schema match {
      case "" => logger.info("empty schema , don`t use userSpecifiedSchema")
      case _ => read.schema(schema)
    }

    format match {
      case "text" => read.text(path).withColumnRenamed("value", "raw_message")
      case "parquet" => read.parquet(path)
      case "json" => read.json(path)
      case "orc" => read.orc(path)
      case "csv" => read.csv(path)
      case _ => read.format(format).load(path)
    }
  }
}

private object File {
  val pathConfig = ConfigEntryBuilder("path").stringConf.create()
  val schemaConfig = ConfigEntryBuilder("schema").stringConf.create(Some(""))
  val formatConfig = ConfigEntryBuilder("format").stringConf.create()
  val optionsConfig = ConfigEntryBuilder("options").stringMapConf.create(Some(Map.empty))
}
