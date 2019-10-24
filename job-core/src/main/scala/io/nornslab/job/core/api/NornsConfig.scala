package io.nornslab.job.core.api

import java.io.File
import java.net.{URI, URL}
import java.util.Properties

import com.typesafe.config.ConfigFactory._
import com.typesafe.config.impl.ConfigImpl
import com.typesafe.config.{Config, ConfigFactory, _}
import com.typesafe.scalalogging.Logger

import scala.collection.JavaConverters._
import scala.concurrent.duration.{Duration, FiniteDuration}
import scala.util.control.NonFatal

/** 配置数据封装
  * 封装 com.typesafe.config.Config 工具类适配 scala ， 使用参考 https://github.com/lightbend/config
  *
  * 参考实现
  * https://github.com/playframework/playframework/blob/master/core/play/src/main/scala/play/api/Configuration.scala
  *
  * For example:
  * {{{
  * val configuration = NornsConfig.load()
  * get[Int]
  * get[Boolean]
  * get[Double]
  * get[Long]
  * get[Number]
  * get[NornsConfig]
  * get[ConfigList]
  *
  * get[Seq[Boolean]]
  * get[Seq[Double]]
  * get[Seq[Int]]
  * get[Seq[Long]]
  * get[Seq[Duration]].map(_.toMillis)
  * get[Seq[Duration]].map(_.toMillis)
  * get[Seq[Number]]
  * get[Seq[String]]
  * get[ConfigObject]
  *
  * underlying.getBytes
  * underlying.getBooleanList
  * underlying.getBytesList
  * underlying.getConfigList
  * underlying.getDoubleList
  * underlying.getIntList
  * underlying.getLongList
  * underlying.getMillisecondsList
  * underlying.getNanosecondsList
  * underlying.getNumberList
  * underlying.getObjectList
  * underlying.getStringList
  * }}}
  *
  */
object NornsConfig {

  def load(classLoader: ClassLoader = this.getClass.getClassLoader,
           properties: Properties = new Properties(),
           directSettings: Map[String, AnyRef] = Map.empty[String, AnyRef],
           applicationConfig: Option[String] = None
          ): NornsConfig = {
    try {
      val userDefinedProperties = if (properties eq System.getProperties) ConfigFactory.empty()
      else parseProperties(properties)

      val combinedConfig: Config = Seq(
        userDefinedProperties,
        ConfigImpl.systemPropertiesAsConfig(),
        parseMap(directSettings.asJava),
        if (applicationConfig.isDefined) parseFile(new File(applicationConfig.get)) else ConfigFactory.empty(),
        parseResources(classLoader, "norns-reference-overrides.conf"),
        defaultReference()
      ).reduceLeft(_.withFallback(_))
        .getConfig("norns")

      val resolvedConfig = combinedConfig.resolve

      NornsConfig(resolvedConfig)
    } catch {
      case e: ConfigException => throw configError(e.getMessage, Option(e.origin), Some(e))
    }
  }

  def loadEmpty: NornsConfig = NornsConfig(ConfigFactory.empty())

  def loadReference: NornsConfig = NornsConfig(defaultReference())

  def loadFrom(data: Map[String, Any]): NornsConfig = {
    def toJava(data: Any): Any = data match {
      case map: Map[_, _] => map.mapValues(toJava).toMap.asJava
      case iterable: Iterable[_] => iterable.map(toJava).asJava
      case v => v
    }
    NornsConfig(parseMap(toJava(data).asInstanceOf[java.util.Map[String, AnyRef]]))
  }

  def apply(data: (String, Any)*): NornsConfig = loadFrom(data.toMap)

  private[api] def configError(message: String,
                               origin: Option[ConfigOrigin] = None,
                               e: Option[Throwable] = None
                              ): Exception = {
    val originLine = origin.map(_.lineNumber: java.lang.Integer).orNull
    val originSourceName = origin.map(_.filename).orNull
    val originUrlOpt = origin.flatMap(o => Option(o.url))
    // todo 待完善异常信息
    new Exception(s"todo config error ${message}")
  }

  private[NornsConfig] def asScalaList[A](l: java.util.List[A]): Seq[A] = asScalaBufferConverter(l).asScala.toList

  private[NornsConfig] val logger = Logger(getClass)
}

/**
  * A full configuration set.
  *
  * The underlying implementation is provided by https://github.com/typesafehub/config.
  *
  * @param underlying the underlying Config implementation
  */
case class NornsConfig(underlying: Config) {

  def ++(other: NornsConfig): NornsConfig = NornsConfig(other.underlying.withFallback(underlying))

  private def readValue[T](path: String, v: => T): Option[T] = try {
    if (underlying.hasPathOrNull(path)) Some(v) else None
  } catch {
    case NonFatal(e) => throw reportError(path, e.getMessage, Some(e))
  }

  def has(path: String): Boolean = underlying.hasPath(path)

  def get[A](path: String)(implicit loader: ConfigLoader[A]): A = loader.load(underlying, path)

  def getAndValidate[A](path: String, values: Set[A])(implicit loader: ConfigLoader[A]): A = {
    val value = get(path)
    if (!values(value)) {
      throw reportError(path, s"Incorrect value, one of (${values.mkString(", ")}) was expected.")
    }
    value
  }

  def getOptional[A](path: String)(implicit loader: ConfigLoader[A]): Option[A] = {
    try {
      if (underlying.hasPath(path)) Some(get[A](path)) else None
    } catch {
      case NonFatal(e) => throw reportError(path, e.getMessage, Some(e))
    }
  }

  /**
    * Retrieves a configuration value as `Milliseconds`.
    * {{{
    * engine.timeout = 1 second
    * }}}
    */
  def getMillis(path: String): Long = get[Duration](path).toMillis

  /**
    * Retrieves a configuration value as `Milliseconds`.
    * {{{
    * engine.timeout = 1 second
    * }}}
    */
  def getNanos(path: String): Long = get[Duration](path).toNanos

  /**
    * Returns available keys.
    *
    * For example:
    * {{{
    * val configuration = NornsConfig.load()
    * val keys = configuration.keys
    * }}}
    *
    * @return the set of keys available in this configuration
    */
  def keys: Set[String] = underlying.entrySet.asScala.map(_.getKey).toSet

  /**
    * Returns sub-keys.
    *
    * For example:
    * {{{
    * val configuration = NornsConfig.load()
    * val subKeys = configuration.subKeys
    * }}}
    *
    * @return the set of direct sub-keys available in this configuration
    */
  def subKeys: Set[String] = underlying.root().keySet().asScala.toSet

  /**
    * Returns every path as a set of key to value pairs, by recursively iterating through the
    * config objects.
    */
  def entrySet: Set[(String, ConfigValue)] = underlying.entrySet().asScala.map(e => e.getKey -> e.getValue).toSet

  /**
    * Creates a configuration error for a specific configuration key.
    *
    * For example:
    * {{{
    * val configuration = NornsConfig.load()
    * throw configuration.reportError("engine.connectionUrl", "Cannot connect!")
    * }}}
    *
    * @param path    the configuration key, related to this error
    * @param message the error message
    * @param e       the related exception
    * @return a configuration exception
    */
  def reportError(path: String, message: String, e: Option[Throwable] = None): Exception = {
    val origin = Option(if (underlying.hasPath(path)) underlying.getValue(path).origin else underlying.root.origin)
    NornsConfig.configError(message, origin, e)
  }

  /**
    * Creates a configuration error for this configuration.
    *
    * For example:
    * {{{
    * val configuration = NornsConfig.load()
    * throw configuration.globalError("Missing configuration key: [yop.url]")
    * }}}
    *
    * @param message the error message
    * @param e       the related exception
    * @return a configuration exception
    */
  def globalError(message: String, e: Option[Throwable] = None): Exception = {
    NornsConfig.configError(message, Option(underlying.root.origin), e)
  }

  val defaultRenderOptions: ConfigRenderOptions = ConfigRenderOptions.defaults
    .setComments(false).setOriginComments(false).setFormatted(true).setJson(true)

  def show: String = underlying.root().render(defaultRenderOptions)

  def show(renderOptions: ConfigRenderOptions): String = underlying.root().render(renderOptions)
}

/**
  * A config loader
  */
trait ConfigLoader[A] {
  self =>
  def load(config: Config, path: String = ""): A

  def map[B](f: A => B): ConfigLoader[B] = (config: Config, path: String) => f(self.load(config, path))

}

object ConfigLoader {

  def apply[A](f: Config => String => A): ConfigLoader[A] = (config: Config, path: String) => f(config)(path)

  import scala.collection.JavaConverters._

  implicit val stringLoader: ConfigLoader[String] = ConfigLoader(_.getString)
  implicit val seqStringLoader: ConfigLoader[Seq[String]] = ConfigLoader(_.getStringList).map(_.asScala.toSeq)
  implicit val stringStringMapLoader: ConfigLoader[Map[String, String]] = ConfigLoader { config =>
    path =>
      config.getConfig(path).entrySet().asScala.map(v => v.getKey -> v.getValue.unwrapped().toString).toMap
  }

  implicit val intLoader: ConfigLoader[Int] = ConfigLoader(_.getInt)
  implicit val seqIntLoader: ConfigLoader[Seq[Int]] = ConfigLoader(_.getIntList).map(_.asScala.map(_.toInt).toSeq)

  implicit val booleanLoader: ConfigLoader[Boolean] = ConfigLoader(_.getBoolean)
  implicit val seqBooleanLoader: ConfigLoader[Seq[Boolean]] =
    ConfigLoader(_.getBooleanList).map(_.asScala.map(_.booleanValue).toSeq)

  implicit val finiteDurationLoader: ConfigLoader[FiniteDuration] =
    ConfigLoader(_.getDuration).map(javaDurationToScala)

  implicit val seqFiniteDurationLoader: ConfigLoader[Seq[FiniteDuration]] =
    ConfigLoader(_.getDurationList).map(_.asScala.map(javaDurationToScala).toSeq)

  implicit val durationLoader: ConfigLoader[Duration] = ConfigLoader { config =>
    path =>
      if (config.getIsNull(path)) Duration.Inf
      else if (config.getString(path) == "infinite") Duration.Inf
      else finiteDurationLoader.load(config, path)
  }

  // Note: this does not support null values but it added for convenience
  implicit val seqDurationLoader: ConfigLoader[Seq[Duration]] =
    seqFiniteDurationLoader.map(identity[Seq[Duration]])

  implicit val doubleLoader: ConfigLoader[Double] = ConfigLoader(_.getDouble)
  implicit val seqDoubleLoader: ConfigLoader[Seq[Double]] =
    ConfigLoader(_.getDoubleList).map(_.asScala.map(_.doubleValue).toSeq)

  implicit val numberLoader: ConfigLoader[Number] = ConfigLoader(_.getNumber)
  implicit val seqNumberLoader: ConfigLoader[Seq[Number]] = ConfigLoader(_.getNumberList).map(_.asScala.toSeq)

  implicit val longLoader: ConfigLoader[Long] = ConfigLoader(_.getLong)
  implicit val seqLongLoader: ConfigLoader[Seq[Long]] =
    ConfigLoader(_.getLongList).map(_.asScala.map(_.longValue).toSeq)

  implicit val bytesLoader: ConfigLoader[ConfigMemorySize] = ConfigLoader(_.getMemorySize)
  implicit val seqBytesLoader: ConfigLoader[Seq[ConfigMemorySize]] =
    ConfigLoader(_.getMemorySizeList).map(_.asScala.toSeq)

  implicit val configLoader: ConfigLoader[Config] = ConfigLoader(_.getConfig)
  implicit val configListLoader: ConfigLoader[ConfigList] = ConfigLoader(_.getList)
  implicit val configObjectLoader: ConfigLoader[ConfigObject] = ConfigLoader(_.getObject)
  implicit val seqConfigLoader: ConfigLoader[Seq[Config]] = ConfigLoader(_.getConfigList).map(_.asScala.toSeq)

  implicit val configurationLoader: ConfigLoader[NornsConfig] = configLoader.map(NornsConfig(_))
  implicit val seqConfigurationLoader: ConfigLoader[Seq[NornsConfig]] = seqConfigLoader.map(_.map(NornsConfig(_)))

  implicit val urlLoader: ConfigLoader[URL] = ConfigLoader(_.getString).map(new URL(_))
  implicit val uriLoader: ConfigLoader[URI] = ConfigLoader(_.getString).map(new URI(_))

  private def javaDurationToScala(javaDuration: java.time.Duration): FiniteDuration =
    Duration.fromNanos(javaDuration.toNanos)

  /**
    * Loads a value, interpreting a null value as None and any other value as Some(value).
    */
  implicit def optionLoader[A](implicit valueLoader: ConfigLoader[A]): ConfigLoader[Option[A]] =
    (config: Config, path: String) => if (config.getIsNull(path)) None else Some(valueLoader.load(config, path))

}