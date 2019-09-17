package io.github.nornslab.norns.core.utils

import java.io.File

import com.typesafe.config.ConfigFactory.{parseFile, parseResources}
import com.typesafe.config.{Config, ConfigException, ConfigFactory, ConfigRenderOptions}

import scala.collection.JavaConverters._

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
    .setComments(false).setOriginComments(false).setFormatted(true).setJson(true)

  def getMap(c: Config, path: String): Map[String, String] = if (c.hasPath(path)) {
    c.getConfig(path).entrySet().asScala.map(f => f.getKey -> f.getValue.unwrapped().toString).toMap
  } else Map.empty

  def render(c: Config): String = c.root().render(renderOptions)
}

/**
  * 配置项
  *
  * @param key         配置项名称
  * @param default     默认值，配置为 None 表示必填
  * @param description 配置描述
  */
case class ConfigKey(key: String,
                     default: Option[_] = None,
                     description: String = "") {

  // 默认校验方法
  @throws[ConfigException.Missing]
  @throws[ConfigException.WrongType]
  def check(c: Config): Unit = {
    // val value: Any = default.get
    // c.getAnyRef(key).isInstanceOf
    val r = c.getString(key)
  }

}