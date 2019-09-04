package io.github.nornslab.norns.core.utils

import java.io.File

import com.typesafe.config.ConfigFactory.{parseFile, parseResources}
import com.typesafe.config.{Config, ConfigFactory, ConfigRenderOptions}

/**
  * @author Li.Wei by 2019/9/4
  */
object ConfigUtils {

  val emptyConfig = ConfigFactory.empty()

  val loadConfFile: Function[(Option[Config], String), Config] = {
    case (None, filePath) => parseResources(filePath)
    case (Some(c), filePathConfigKey) if c.hasPathOrNull(filePathConfigKey) =>
      parseFile(new File(c.getString(filePathConfigKey))) // TODO file 获取失败后默认以resource方式获取
    case _ => emptyConfig
  }

  val renderOptions = ConfigRenderOptions.defaults
    .setComments(false).setOriginComments(false).setFormatted(true).setJson(false)

}
