package com.gourd.norns.core

import java.io.File

import com.gourd.norns.core.utils.Logging
import com.typesafe.config.ConfigFactory._
import com.typesafe.config.{Config, ConfigRenderOptions}

/**
  * @author Li.Wei by 2019/8/29
  */
trait Job extends Logging {

  private lazy val _config: Config = {
    val sysConf = empty()
      .withFallback(systemEnvironment)
      .withFallback(systemProperties)

    sysConf
      .withFallback(Job.loadConfFile(None -> Job.defaultConfigConf))
      .withFallback(Job.loadConfFile(None -> Job.nornsConf))
      .withFallback(Job.loadConfFile(Some(sysConf) -> Job.jobConfig))
      .withOnlyPath(Job.norns)
  }

  def config: Config = _config

  /** [[Job.run()]] 运行前打开资源 */
  def open(): Unit = {
    info(config.root().render(Job.renderOptions))
  }

  /** job 运行 */
  def run(): Unit

}

object Job extends Logging {

  val norns = "norns"
  val jobConfig = s"$norns.job.config"
  val defaultConfigConf = "default.norns.conf"
  val nornsConf = "norns.conf"

  val renderOptions = ConfigRenderOptions.defaults
    .setComments(false).setOriginComments(false).setFormatted(true).setJson(true)

  val loadConfFile: Function[(Option[Config], String), Config] = {
    case (None, filePath) => parseResources(filePath)
    case (Some(c), filePathConfigKey) if c.hasPathOrNull(filePathConfigKey) =>
      parseFile(new File(c.getString(filePathConfigKey)))
    case _ => empty()
  }
}

