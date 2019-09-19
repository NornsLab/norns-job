package io.github.nornslab.norns.core.api

import com.typesafe.config.{Config, ConfigFactory}
import io.github.nornslab.norns.core.api.base.CoreConfigKeys
import io.github.nornslab.norns.core.utils.{ConfigUtils, Logging}

/** 上下文内容
  *
  * @author Li.Wei by 2019/8/29
  */
trait Context extends Logging with AutoCloseable {

  def name: String = this.getClass.getCanonicalName

  /** 当前上下文内容提供配置信息 */
  def config: Config = Context.defaultLoadConfig

  override def close(): Unit = {}
}

private object Context extends Logging {

  /** 读取 job 配置文件路径 */
  val nornsJobConfig = s"norns.job.config"

  /** 默认载入 job 配置文件 */
  val nornsJobConf = "norns-job.conf"
  val nornsJobJson = "norns-job.json"
  val nornsJobProperties = "norns-job.properties"

  /**
    * 为简化配置操作，不引用 main 函数传入 args参数，推荐使用系统参数（-D）或者配置文件
    * =配置装载顺序=
    * 系统环境变量              以norns最为前缀的配置
    * 系统参数 -D              以norns最为前缀的配置
    * 默认配置文件（非必须）      norns-job.conf , norns-job.json , norns-job.properties
    * 自定义配置文件           （使用 -Dnorns.job.config=${path} 指定）
    */
  private val defaultLoadConfig: Config = {
    val sysConf = ConfigFactory.empty()
      .withFallback(ConfigFactory.systemEnvironment)
      .withFallback(ConfigFactory.systemProperties)
      .withOnlyPath(CoreConfigKeys.norns)

    val r = sysConf
      .withFallback(ConfigUtils.loadConfFile(None -> nornsJobConf))
      .withFallback(ConfigUtils.loadConfFile(None -> nornsJobJson))
      .withFallback(ConfigUtils.loadConfFile(None -> nornsJobProperties))
      .withFallback(ConfigUtils.loadConfFile(Some(sysConf) -> nornsJobConfig))
    info(s"Context defaultLoadConfig=\n${r.root().render(ConfigUtils.renderOptions)}")
    r
  }
}
