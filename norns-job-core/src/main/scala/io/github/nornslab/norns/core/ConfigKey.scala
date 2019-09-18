package io.github.nornslab.norns.core

import com.typesafe.config.{Config, ConfigException}

/**
  * 配置项
  *
  * @param key         配置项名称
  * @param default     默认值，配置为 None 表示必填
  * @param description 配置描述
  * @author Li.Wei by 2019/9/2
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
