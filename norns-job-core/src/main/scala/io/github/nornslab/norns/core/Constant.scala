package io.github.nornslab.norns.core

import java.io.File

import com.typesafe.config.ConfigFactory.{empty, parseFile, parseResources}
import com.typesafe.config.{Config, ConfigRenderOptions}

/** norns.core Job 常量
  *
  * @author Li.Wei by 2019/8/29
  */
object Constant {
  val norns = "norns"
  val nornsCore = s"$norns.core"

  val renderOptions = ConfigRenderOptions.defaults
    .setComments(false).setOriginComments(false).setFormatted(true).setJson(false)

  val loadConfFile: Function[(Option[Config], String), Config] = {
    case (None, filePath) => parseResources(filePath)
    case (Some(c), filePathConfigKey) if c.hasPathOrNull(filePathConfigKey) =>
      parseFile(new File(c.getString(filePathConfigKey))) // TODO file 获取失败后默认以resource方式获取
    case _ => empty()
  }
}
