package io.github.nornslab.norns.core.api

/** 插件配置信息封装
  *
  * @param key          配置key名称
  * @param classType    key 类型
  * @param defaultValue 默认值
  * @param description  描述
  * @param deprecated   废弃状态
  * @tparam T object, list, number, boolean, null, string
  * @author Li.Wei by 2019/9/19
  */
case class PluginConfigSpec[T](key: String,
                               classType: Class[T],
                               defaultValue: Option[T] = None,
                               description: String = "",
                               deprecated: Boolean = false
                              ) {
  // 不提供默认值表示必填
  val required: Boolean = defaultValue.isDefined
}

object PluginConfigSpec {

  def string(key: String): PluginConfigSpec[String] =
    PluginConfigSpec[String](key, classOf[String])

  def string(key: String, defaultValue: String): PluginConfigSpec[String] =
    PluginConfigSpec[String](key, classOf[String], Some(defaultValue))

  /* def int(key: String): PluginConfigSpec[Int] =
    PluginConfigSpec[Int](key, classOf[Int])

  def int(key: String, defaultValue: Int): PluginConfigSpec[Int] =
    PluginConfigSpec[Int](key, classOf[Int], Some(defaultValue))
    */

  def number(key: String, defaultValue: Number): PluginConfigSpec[Number] =
    PluginConfigSpec[Number](key, classOf[Number], Some(defaultValue))
}
