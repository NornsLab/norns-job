package io.github.nornslab.norns.spark.plugins.input

import java.util

import io.github.nornslab.norns.core.api.{Configuration, Input, PluginConfigSpec}
import io.github.nornslab.norns.core.plugins.input.BaseFile
import io.github.nornslab.norns.spark.SJC
import io.github.nornslab.norns.spark.plugins.input.FilePluginConfigSpec.{formatConfigSpec, optionsConfigSpec, pathConfigSpec}
import org.apache.spark.sql.{Dataset, Row}

/**
  * @author Li.Wei by 2019/9/5
  */
class File(override val pluginConfig: Configuration,
           override val context: SJC,
           override val data: Map[String, AnyRef])
  extends BaseFile[Dataset[Row]](pluginConfig, context, data) with Input[Dataset[Row]] {

  val path = s"""file://${pluginConfig.get(pathConfigSpec)}"""
  val format = pluginConfig.get(formatConfigSpec)
  val options = pluginConfig.get(optionsConfigSpec)

  override def configSchema: Seq[PluginConfigSpec[_]] = Seq(pathConfigSpec, formatConfigSpec, optionsConfigSpec)

  override def input: Dataset[Row] = {
    val read = context.sparkSession.read.options(options)
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
  val formatConfigSpec = PluginConfigSpec.string("format")
  val optionsConfigSpec = PluginConfigSpec[util.HashMap[String, String]](
    "options",
    classOf[util.HashMap[String, String]],
    Some(new util.HashMap())
  )
}
