package io.github.nornslab.norns.spark.plugins.input

import io.github.nornslab.norns.core.api.{ConfigEntry, Configuration, TaskContext}
import io.github.nornslab.norns.core.plugins.input.BaseFile
import io.github.nornslab.norns.spark.SJC
import io.github.nornslab.norns.spark.plugins.PluginConfigEntry._
import org.apache.spark.sql.{Dataset, Row}

/**
  * @author Li.Wei by 2019/9/5
  */
class File(implicit override val pluginConfig: Configuration,
           implicit override val jc: SJC,
           implicit override val tc: TaskContext)
  extends BaseFile[SJC, Dataset[Row]] {

  val path = s"""file://${pluginConfig.get(pathConfig)}"""
  val schema = pluginConfig.get(schemaConfig)
  val format = pluginConfig.get(formatConfig)
  val options = pluginConfig.get(optionsConfig)

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

object Test {
  def main(args: Array[String]): Unit = {
    val file = new java.io.File("./")
    println(file.listFiles())
  }
}