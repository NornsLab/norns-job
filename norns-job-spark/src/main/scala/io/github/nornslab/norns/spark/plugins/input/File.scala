package io.github.nornslab.norns.spark.plugins.input

import java.util
import java.util.Collections

import io.github.nornslab.norns.core.api.{Configuration, PluginConfigSpec, TaskContext}
import io.github.nornslab.norns.core.plugins.input.BaseFile
import io.github.nornslab.norns.spark.SJC
import io.github.nornslab.norns.spark.plugins.input.FilePluginConfigSpec._
import org.apache.spark.sql.{Dataset, Row}

/**
  * @author Li.Wei by 2019/9/5
  */
class File(implicit override val pluginConfig: Configuration,
           implicit override val jc: SJC,
           implicit override val tc: TaskContext)
  extends BaseFile[SJC, Dataset[Row]] {

  val path = s"""file://${pluginConfig.get(pathConfigSpec)}"""
  val schema = pluginConfig.get(schemaConfigSpec)
  val format = pluginConfig.get(formatConfigSpec)
  val options = pluginConfig.get(optionsConfigSpec)

  override def configSchema: Seq[PluginConfigSpec[_]] = Seq(pathConfigSpec, formatConfigSpec, optionsConfigSpec)

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

/* ------------------------------------------------------------------------------------- *
   File 插件支持配置项
 * ------------------------------------------------------------------------------------- */
private object FilePluginConfigSpec {
  val pathConfigSpec = PluginConfigSpec.string("path")
  val schemaConfigSpec = PluginConfigSpec.string("schema", "")
  val formatConfigSpec = PluginConfigSpec.string("format")
  val optionsConfigSpec = PluginConfigSpec[util.Map[String, String]](
    "options",
    classOf[util.Map[String, String]],
    Some(Collections.emptyMap[String, String]())
  )
}
