package io.github.nornslab.norns.spark.plugins

import io.github.nornslab.norns.core.api.ConfigEntryBuilder

/**
  * @author Li.Wei by 2019/10/23
  */
object PluginConfigEntry {

  val pathConfig = ConfigEntryBuilder("path").stringConf.create()
  val schemaConfig = ConfigEntryBuilder("schema").stringConf.create(Some(""))
  val formatConfig = ConfigEntryBuilder("format").stringConf.create()
  val optionsConfig = ConfigEntryBuilder("options").stringMapConf.create(Some(Map.empty[String, String]))
  val outputModeConfig = ConfigEntryBuilder("outputMode").stringConf.create()
}
