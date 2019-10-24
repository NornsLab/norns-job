package io.nornslab.job.core.api

/** 插件配置信息封装
  * 参考 org.apache.spark.internal.config
  *
  * @example
  * {{{
  *
  * val limitConfigSpec = ConfigEntryBuilder("limit").intConf.checkValue(_ > 0, "limit must be > 0").create(Some(20))
  *
  * val triggerConfig = ConfigEntryBuilder("trigger")
  *     .doc {
  *       "support [Continuous, ProcessingTime, Once] . e.g. Continuous#10 seconds, ProcessingTime#10 seconds, Once"
  *     }
  *     .conf({ value =>
  *       val triggerSetting = value.split("#")
  *       triggerSetting(0) match {
  *         case "Continuous" => Trigger.Continuous(triggerSetting(1))
  *         case "ProcessingTime" => Trigger.Continuous(triggerSetting(1))
  *         case "Once" => Trigger.Once()
  *       }
  *     }).create(Some(Trigger.ProcessingTime(0)))
  *
  * }}}
  * @param key                配置key名称
  * @param valueConverter     NornsConfig => T
  * @param defaultValue       配置值默认值，默认为 None
  * @param dynamicReplacement 是否支持数据值动态替换，默认为 false
  * @param stringConverter    T => String ，默认调用 toString
  * @param doc                描述文档，默认为 ""
  * @param isPublic           是否发布给用户进行配置，默认为 true
  * @tparam T 配置项数据类型
  * @author Li.Wei by 2019/9/19
  */
case class ConfigEntry[T](key: String,
                          valueConverter: NornsConfig => T,
                          defaultValue: Option[T] = None,
                          dynamicReplacement: Boolean = false,
                          stringConverter: T => String = (t: T) => t.toString,
                          doc: String = "",
                          isPublic: Boolean = true) {

  // 不提供默认值表示必填
  lazy val required: Boolean = defaultValue.isDefined

  /**
    * 如果 dynamicReplacement 为 false ，则提取数据值时调用该方法
    *
    * @param reader reader
    * @return T
    */
  def readFrom(reader: NornsConfig): T =
    if (reader.has(key)) valueConverter(reader)
    else {
      if (defaultValue.isDefined) defaultValue.get
      else throw new IllegalStateException(s"error , NornsConfig get defaultValue [$key] miss , ConfigEntry:$this")
    }

  /**
    * 如果 dynamicReplacement 为 true ，则提取数据值时调用该方法
    * 针对配置内容做数据动态替换
    * 替换规则如下
    * %VAL{configKey} 将从jobContext.config ,taskContext.config 查找对应 configKey 执行替换操作 [[ConfigEntry.matchConfig()]]
    *
    * @example
    * {{{
    *    val sql = "SELECT * FROM %VAL{tableName.aa} WHERE app = %VAL{app.Id} AND time > 1"
    *    val result = matchConfig[String](sql, NornsConfig.loadFrom(Map("tableName.aa" -> "TABLE", "app.Id" -> 1)))
    *    println(result) // SELECT * FROM TABLE WHERE app = 1 AND time > 1
    * }}}
    * @param reader      NornsConfig
    * @param jobContext  jobContext
    * @param taskContext taskContext
    * @return t
    */
  def readFrom(reader: NornsConfig, jobContext: JobContext, taskContext: TaskContext): T =
    ConfigEntry.matchConfig[T](
      ConfigEntry.matchConfig[T](readFrom(reader), jobContext.config),
      taskContext.nornsConfig
    )
}

private object ConfigEntry {

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

case class ConfigEntryBuilder(key: String) {
  private[api] var _dynamicReplacement = false
  private[api] var _public = true
  private[api] var _doc = ""

  def supportDynamicReplacement(): ConfigEntryBuilder = {
    _dynamicReplacement = true
    this
  }

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

  def seqStringConf: TypedConfigBuilder[Seq[String]] = new TypedConfigBuilder(this, _.get[Seq[String]](key))

  def stringMapConf: TypedConfigBuilder[Map[String, String]] =
    new TypedConfigBuilder(this, _.get[Map[String, String]](key))

  // 自定义数据值提取为 string ，并转换为 E
  def conf[E](valueConverter: String => E): TypedConfigBuilder[E] =
    new TypedConfigBuilder(this, (config: NornsConfig) => valueConverter(config.get[String](key)))

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
    parent._dynamicReplacement,
    stringConverter,
    parent._doc,
    parent._public
  )

}