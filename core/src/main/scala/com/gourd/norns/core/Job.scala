package com.gourd.norns.core

import java.io.File

import com.gourd.norns.core.Job.{CONFIG_RENDER_OPTIONS_JSON, configPath, defaultConfigPath}
import com.gourd.norns.core.utils.Logging
import com.typesafe.config.ConfigFactory.{empty, parseFile}
import com.typesafe.config.{Config, ConfigFactory, ConfigRenderOptions}

/**
  * @author Li.Wei by 2019/8/29
  */
trait Job extends Logging {

  lazy val config: Config = {
    val defaultConfig = empty()
    // .withFallback(systemEnvironment)
    // .withFallback(systemProperties)

    val config1 = defaultConfig
      .withFallback(Job.loadFile.apply(None -> defaultConfigPath))
      .withFallback(Job.loadFile.apply(Some(defaultConfig) -> configPath))
    info(config1.root.render(CONFIG_RENDER_OPTIONS_JSON))
    val r = config1.withOnlyPath("norns")
    info(r.root.render(CONFIG_RENDER_OPTIONS_JSON))
    r
  }

  /** [[Job.run()]] 运行前打开资源 */
  def open(): Unit = {}

  /** job 运行 */
  def run(): Unit

}

object Job extends Logging {

  val configPath = "norns.job.config.path"
  val defaultConfigPath = "norns.conf"
  val CONFIG_RENDER_OPTIONS_JSON =
    ConfigRenderOptions.defaults.setComments(false).setOriginComments(false).setFormatted(true).setJson(true)

  val loadFile: PartialFunction[(Option[Config], String), Config] = {
    case (None, filePath) =>
      info("case1" + filePath)
      ConfigFactory.parseResources("norns.conf")
    case (Some(c), filePathConfigKey) if c.hasPathOrNull(filePathConfigKey) =>
      parseFile(new File(c.getString(filePathConfigKey)))
    case _ => empty()
  }

}


