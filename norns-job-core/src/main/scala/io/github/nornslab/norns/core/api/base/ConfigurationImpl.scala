package io.github.nornslab.norns.core.api.base

import io.github.nornslab.norns.core.api._
import io.github.nornslab.norns.core.api.base.ConfigurationImpl.matchConfig

/**
  * @author Li.Wei by 2019/9/19
  */
class ConfigurationImpl(config: NornsConfig) extends Configuration {

  override def get[T](pce: PluginConfigEntry[T]): T = {
    val classType: Class[T] = pce.classType
    val key = pce.key
    val v = if (config.has(key)) { // 获取返回值
      classType.cast(config.underlying.getAnyRef(key))
    } else {
      if (pce.defaultValue.isDefined) pce.defaultValue.get
      else throw new IllegalStateException(s"error , Configuration get defaultValue [$key] miss")
    }
    if (pce.checkFunc.apply(v)) v // 对返回值做数据校验
    else throw new IllegalStateException(s"error , Configuration check PluginConfigEntry [$pce] value [$v]")
  }

  /**
    * 针对配置内容做数据动态替换
    * 替换规则如下
    * %VAL{configKey} 将从jobContext.config ,taskContext.config 查找对应 configKey 执行替换操作 [[ConfigurationImpl.matchConfig()]]
    *
    * @example
    * {{{
    *    val sql = "SELECT * FROM %VAL{tableName.aa} WHERE app = %VAL{app.Id} AND time > 1"
    *    val result = matchConfig[String](sql, NornsConfig.loadFrom(Map("tableName.aa" -> "TABLE", "app.Id" -> 1)))
    *    println(result) // SELECT * FROM TABLE WHERE app = 1 AND time > 1
    * }}}
    * @param pce         PluginConfigEntry
    * @param jobContext  jobContext
    * @param taskContext taskContext
    * @tparam T 数据类型
    * @return t
    */
  override def get[T](pce: PluginConfigEntry[T], jobContext: JobContext, taskContext: TaskContext): T =
    matchConfig[T](
      matchConfig[T](get[T](pce), jobContext.config),
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
