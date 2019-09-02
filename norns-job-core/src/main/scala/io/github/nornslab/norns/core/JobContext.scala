package io.github.nornslab.norns.core

import com.typesafe.config.{Config, ConfigFactory}
import io.github.nornslab.norns.core.utils.Logging

/**
  * @author Li.Wei by 2019/9/2
  */
trait JobContext extends Context {

  def config: Config = JobContext.defaultLoadConfig
}

object JobContext extends Logging {
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
      .withOnlyPath(Constant.norns)

    val r = sysConf
      .withFallback(Constant.loadConfFile(None -> nornsJobConf))
      .withFallback(Constant.loadConfFile(None -> nornsJobJson))
      .withFallback(Constant.loadConfFile(None -> nornsJobProperties))
      .withFallback(Constant.loadConfFile(Some(sysConf) -> nornsJobConfig))
    // info(r.root().render(Constant.renderOptions)) // log
    r
  }
}


case class EmptyJobContext() extends JobContext