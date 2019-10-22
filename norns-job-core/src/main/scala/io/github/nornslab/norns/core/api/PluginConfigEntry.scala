package io.github.nornslab.norns.core.api

/** 插件配置信息封装
  *
  * @param key          配置key名称
  * @param defaultValue 默认值
  * @param doc          描述文档
  * @param deprecated   废弃状态
  * @param isPublic     是否发布给用户进行配置
  * @tparam T object, list, number, boolean, null, string
  * @author Li.Wei by 2019/9/19
  */
case class PluginConfigEntry[T](key: String,
                                defaultValue: Option[T] = None,
                                checkFunc: T => Boolean = _ => true,
                                doc: String = "",
                                deprecated: Boolean = false,
                                isPublic: Boolean = true
                               ) {
  lazy val classType: Class[T] = classOf[T]
  // 不提供默认值表示必填
  lazy val required: Boolean = defaultValue.isDefined

}

/** 快速构建 [[PluginConfigEntry]] 信息 */
object PluginConfigEntry {

  def string(key: String): PluginConfigEntry[String] =
    PluginConfigEntry[String](key)

  def string(key: String, defaultValue: String): PluginConfigEntry[String] =
    PluginConfigEntry[String](key, Some(defaultValue))

  def number(key: String, defaultValue: Number): PluginConfigEntry[Number] =
    PluginConfigEntry[Number](key, Some(defaultValue))
}
