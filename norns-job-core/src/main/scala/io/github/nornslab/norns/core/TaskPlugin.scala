package io.github.nornslab.norns.core

import com.typesafe.config.{Config, ConfigFactory}
import io.github.nornslab.norns.core.utils.ConfigUtils.emptyConfig
import io.github.nornslab.norns.core.utils.{ConfigKey, ConfigUtils}

import scala.util.Try

/** 任务插件
  *
  * @author Li.Wei by 2019/9/2
  */
trait TaskPlugin extends Service {

  // 插件配置信息
  val pluginConfig: Config

}

/**
  * [[TaskPlugin]] 基础实现
  *
  * @param _pluginInitConfig 插件初始化配置信息
  * @param _tc               插件依赖数据参数
  * @tparam JC JobContext
  */
class BaseTaskPlugin[JC <: JobContext](private implicit val _pluginInitConfig: Config = emptyConfig,
                                       private implicit val _tc: (JC, Config))
  extends TaskPlugin {

  import scala.collection.JavaConverters._

  /** 插件配置信息 = 初始化插件配置信息+默认配置信息（非必填默认进行配置填充） */
  val pluginConfig: Config = {
    val map = supportConfig.filter(_.default.isDefined).map(c => c.key -> c.default.get).toMap
    val config = _pluginInitConfig.withFallback(ConfigFactory.parseMap(map.asJava))
    info(s"$name config \n${ConfigUtils.render(config)}")
    config
  }

  // 插件运行依赖数据配置
  val dataConfig: Config = _tc._2

  override type C = JC

  override def context: JC = _tc._1

  /** 取所有支持配置项中必填项进行配置项存在与否校验 */
  override def init: Try[this.type] = Try {
    supportConfig.filter(_.default.isEmpty).foreach(_.check(pluginConfig))
    this
  }

  /** 插件支持配置项 */
  def supportConfig: Seq[ConfigKey] = Seq.empty

}


/** Input
  *
  * @tparam TPD 插件处理流程依赖数据结构
  */
trait Input[TPD] extends TaskPlugin {
  def input: TPD
}

/** Filter
  *
  * @tparam TPD 插件处理流程依赖数据结构
  */
trait Filter[TPD] extends TaskPlugin {
  def filter(d: TPD): TPD
}

/** Output
  *
  * @tparam TPD 插件处理流程依赖数据结构
  */
trait Output[TPD] extends TaskPlugin {
  def output(d: TPD): Unit
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

