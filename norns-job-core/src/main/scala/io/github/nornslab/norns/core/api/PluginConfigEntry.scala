package io.github.nornslab.norns.core.api

import scala.reflect.ClassTag

/** 插件配置信息封装
  *
  * @param key            配置key名称
  * @param valueClassType valueClassType todo 采用反射获取泛型 T 对应classType
  * @param defaultValue   默认值
  * @param doc            描述文档
  * @param deprecated     废弃状态
  * @param isPublic       是否发布给用户进行配置
  * @tparam T object, list, number, boolean, null, string
  * @author Li.Wei by 2019/9/19
  */
case class PluginConfigEntry[T: ClassTag](key: String,
                                          valueClassType: Class[T],
                                          defaultValue: Option[T] = None,
                                          checkFunc: T => Boolean = (_: T) => true,
                                          doc: String = "",
                                          deprecated: Boolean = false,
                                          isPublic: Boolean = true
                                         ) {

  // 不提供默认值表示必填
  lazy val required: Boolean = defaultValue.isDefined

}

/** 快速构建 [[PluginConfigEntry]] 信息 */
object PluginConfigEntry {

  def string(key: String): PluginConfigEntry[String] =
    PluginConfigEntry[String](key, classOf[String])

  def string(key: String, defaultValue: String): PluginConfigEntry[String] =
    PluginConfigEntry[String](key, classOf[String], Some(defaultValue))

  def number(key: String, defaultValue: Number): PluginConfigEntry[Number] =
    PluginConfigEntry[Number](key, classOf[Number], Some(defaultValue))
}
