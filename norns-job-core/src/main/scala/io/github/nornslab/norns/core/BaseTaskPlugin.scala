package io.github.nornslab.norns.core

import com.typesafe.config.{Config, ConfigFactory}
import io.github.nornslab.norns.core.api._
import io.github.nornslab.norns.core.utils.ConfigUtils
import io.github.nornslab.norns.core.utils.ConfigUtils.emptyConfig

import scala.util.{Failure, Try}


/**
  * [[Plugin]] 基础实现
  *
  * @param _pluginInitConfig 插件初始化配置信息
  * @param _tc               插件依赖数据参数
  * @tparam JC JobContext
  * @author Li.Wei by 2019/9/2
  */
class BaseTaskPlugin[JC <: JobContext](private implicit val _pluginInitConfig: Config = emptyConfig,
                                       private implicit val _tc: (JC, Config))
  extends Plugin {
  self =>
  override type C = JC

  override def context: JC = _tc._1

  // 插件运行依赖数据配置
  val dataConfig: Config = _tc._2

  import scala.collection.JavaConverters._

  // 插件配置信息 = 初始化插件配置信息+默认配置信息（非必填默认进行配置填充）todo 覆盖问题待测试
  val pluginConfig: Config = {
    val config = ConfigFactory.parseMap {
      supportConfig.filter(_.default.isDefined).map(c => c.key -> c.default.get).toMap.asJava
    }.withFallback(_pluginInitConfig)
    info(
      s"""$name
         |pluginConfig=
         |${ConfigUtils.render(config)}
         |dataConfig=
         |${ConfigUtils.render(dataConfig)}""".stripMargin)
    config
  }

  /** 取所有支持配置项中必填项进行配置项存在与否校验 */
  override def init: Option[Throwable] = {
    val exceptions = supportConfig
      .filter(_.default.isEmpty)
      .map(k => Try(k.check(pluginConfig)) match {
        case Failure(exception) => Some(exception)
        case _ => None
      })
      .filter(_.isDefined).map(_.get)

    if (exceptions.isEmpty) None
    else Some(
      exceptions.fold[Throwable](new Exception(
        s"""init $name error,
           | pluginConfig =${ConfigUtils.render(pluginConfig)}
           | supportConfig=$supportConfig
            """.stripMargin))((e1: Throwable, e2: Throwable) => {
        e1.addSuppressed(e2)
        e1
      })
    )
  }

  /** 插件支持配置项 */
  def supportConfig: Seq[ConfigKey] = Seq.empty

}


abstract class BaseInput[JC <: JobContext, TPD](private implicit val _pluginInitConfig: Config = emptyConfig,
                                                private implicit val _tc: (JC, Config))
  extends BaseTaskPlugin[JC] with Input[TPD]

abstract class BaseFilter[JC <: JobContext, TPD](private implicit val _pluginInitConfig: Config = emptyConfig,
                                                 private implicit val _tc: (JC, Config))
  extends BaseTaskPlugin[JC] with Filter[TPD]

abstract class BaseOutput[JC <: JobContext, TPD](private implicit val _pluginInitConfig: Config = emptyConfig,
                                                 private implicit val _tc: (JC, Config))
  extends BaseTaskPlugin[JC] with Output[TPD]

