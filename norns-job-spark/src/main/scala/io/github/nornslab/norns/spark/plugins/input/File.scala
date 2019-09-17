package io.github.nornslab.norns.spark.plugins.input

import java.util

import com.typesafe.config.Config
import io.github.nornslab.norns.core.utils.{ConfigKey, ConfigUtils}
import io.github.nornslab.norns.spark.SJC
import io.github.nornslab.norns.spark.plugins.SparkInput
import org.apache.spark.sql.{Dataset, Row}

/**
  * @author Li.Wei by 2019/9/5
  */
class File(private implicit val _pluginInitConfig: Config,
           private implicit val _tc: (SJC, Config))
  extends SparkInput {

  override def supportConfig: Seq[ConfigKey] = Seq(FileConfigKeys.path, FileConfigKeys.format, FileConfigKeys.options)

  override def input: Dataset[Row] = {
    val path: String = s"""file://${pluginConfig.getString(FileConfigKeys.path.key)}"""
    val format: String = pluginConfig.getString(FileConfigKeys.format.key)
    val options = ConfigUtils.getMap(pluginConfig, FileConfigKeys.options.key)

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
object FileConfigKeys {

  val path = ConfigKey(key = "path", description = "file path")
  val format = ConfigKey(key = "format", default = Some("json"))
  val options = ConfigKey(key = "options", default = Some(new util.HashMap()))

}

