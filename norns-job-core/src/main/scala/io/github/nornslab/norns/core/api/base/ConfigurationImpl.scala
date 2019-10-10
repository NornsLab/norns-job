package io.github.nornslab.norns.core.api.base

import io.github.nornslab.norns.core.api._
import io.github.nornslab.norns.core.api.base.ConfigurationImpl.matchConfig

/**
  * @author Li.Wei by 2019/9/19
  */
class ConfigurationImpl(config: NornsConfig) extends Configuration {

  override def get[T](pcs: PluginConfigSpec[T]): T = {
    val classType: Class[T] = pcs.classType
    val key = pcs.key
    if (config.has(key)) {
      classType.cast(config.underlying.getAnyRef(key))
    } else {
      if (pcs.defaultValue.isDefined) pcs.defaultValue.get
      else throw new IllegalStateException(s"error , Configuration get defaultValue [$key] miss")
    }
  }

  /**
    * @example
    * {{{
    *    val sql = "SELECT * FROM %VAL{tableName.aa} WHERE app = %VAL{app.Id} AND time > 1"
    *    val result = matchConfig[String](sql, NornsConfig.loadFrom(Map("tableName.aa" -> "TABLE", "app.Id" -> 1)))
    *    println(result) // SELECT * FROM TABLE WHERE app = 1 AND time > 1
    * }}}
    * @param configSpec  configSpec
    * @param jobContext  jobContext
    * @param taskContext taskContext
    * @tparam T 数据类型
    * @return t
    */
  override def get[T](configSpec: PluginConfigSpec[T], jobContext: JobContext, taskContext: TaskContext): T =
    matchConfig[T](
      matchConfig[T](get[T](configSpec), jobContext.config),
      taskContext.nornsConfig
    )
}


object ConfigurationImpl {

  val regex = "%VAL\\{(.*?)\\}.*?".r
  val regexPrefix = "%VAL{"

  def matchConfig[T](t: T, nornsConfig: NornsConfig): T = {
    var value = t.toString
    var m = regex.findFirstMatchIn(value)
    while (m.isDefined) {
      val matched = m.get.matched
      val key = matched.substring(regexPrefix.length, matched.length - 1)
      value = value.replace(matched, nornsConfig.get[String](key))
      m = regex.findFirstMatchIn(value)
    }
    value.asInstanceOf[T]
  }
}
