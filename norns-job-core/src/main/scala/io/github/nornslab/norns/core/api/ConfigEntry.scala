package io.github.nornslab.norns.core.api

/** 插件配置信息封装
  * 参考 org.apache.spark.internal.config
  *
  * @example
  * {{{
  * val limitConfigSpec = ConfigEntryBuilder("limit").intConf.checkValue(_ > 0, "limit must be > 0").create(Some(20))
  * }}}
  * @param key             配置key名称
  * @param valueConverter  NornsConfig => T
  * @param defaultValue    配置值默认值，默认为 None
  * @param stringConverter T => String ，默认调用 toString
  * @param doc             描述文档，默认为 ""
  * @param isPublic        是否发布给用户进行配置，默认为 true
  * @tparam T 配置项数据类型
  * @author Li.Wei by 2019/9/19
  */
case class ConfigEntry[T](key: String,
                          valueConverter: NornsConfig => T,
                          defaultValue: Option[T] = None,
                          stringConverter: T => String = (t: T) => t.toString,
                          doc: String = "",
                          isPublic: Boolean = true) {

  // 不提供默认值表示必填
  lazy val required: Boolean = defaultValue.isDefined

  private[api] def readFrom(reader: NornsConfig): T =
    if (reader.has(key)) valueConverter(reader)
    else {
      if (defaultValue.isDefined) defaultValue.get
      else throw new IllegalStateException(s"error , Configuration get defaultValue [$key] miss")
    }
}

case class ConfigEntryBuilder(key: String) {
  private[api] var _public = true
  private[api] var _doc = ""

  def internal(): ConfigEntryBuilder = {
    _public = false
    this
  }

  def doc(doc: String): ConfigEntryBuilder = {
    _doc = doc
    this
  }

  // 提供快速创建方法模板

  def intConf: TypedConfigBuilder[Int] = new TypedConfigBuilder(this, _.get[Number](key).intValue())

  def longConf: TypedConfigBuilder[Long] = new TypedConfigBuilder(this, _.get[Number](key).longValue())

  def doubleConf: TypedConfigBuilder[Double] = new TypedConfigBuilder(this, _.get[Number](key).doubleValue())

  def booleanConf: TypedConfigBuilder[Boolean] = new TypedConfigBuilder(this, _.get[Boolean](key))

  def stringConf: TypedConfigBuilder[String] = new TypedConfigBuilder(this, _.get[String](key))

  def stringMapConf: TypedConfigBuilder[Map[String, String]] =
    new TypedConfigBuilder(this, _.get[Map[String, String]](key))

}

class TypedConfigBuilder[T](val parent: ConfigEntryBuilder,
                            val valueConverter: NornsConfig => T,
                            val stringConverter: T => String = (t: T) => t.toString) {

  /** 应用该函数实现数据转换包装. */
  def transform(fn: T => T): TypedConfigBuilder[T] =
    new TypedConfigBuilder(parent, s => fn(valueConverter(s)), stringConverter)

  /** 添加数据值校验. */
  def checkValue(validator: T => Boolean, errorMsg: String): TypedConfigBuilder[T] =
    transform { v =>
      if (!validator(v)) {
        throw new IllegalArgumentException(s"config checkValue key=[${parent.key}], value=[$v], errorMsg=$errorMsg")
      }
      v
    }

  /** 添加多个数据值校验. */
  def checkValues(validValues: Set[T]): TypedConfigBuilder[T] =
    transform { v =>
      if (!validValues.contains(v)) {
        throw new IllegalArgumentException(
          s"The value of ${parent.key} should be one of ${validValues.mkString(", ")}, but was $v")
      }
      v
    }

  def create(defaultValue: Option[T] = None): ConfigEntry[T] = ConfigEntry[T](
    parent.key,
    valueConverter,
    defaultValue,
    stringConverter,
    parent._doc,
    parent._public
  )

}