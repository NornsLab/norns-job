package io.github.nornslab.norns.core.api

import io.github.nornslab.norns.core.utils.Logging

/**
  * @author Li.Wei by 2019/9/30
  */
trait JobContext extends Context {

  /** 当前上下文内容提供配置信息 */
  def config: NornsConfig = JobContext.defaultLoadConfig
}

private object JobContext extends Logging {

  /** 读取 job 配置文件路径 */
  val nornsJobConfig = s"norns.job.config"

  /**
    * 为简化配置操作，不引用 main 函数传入 args参数，推荐使用系统参数（-D）或者配置文件
    * =配置装载顺序=
    * 系统环境变量              以norns最为前缀的配置
    * 系统参数 -D              以norns最为前缀的配置
    * 默认配置文件（非必须）      norns-job.conf , norns-job.json , norns-job.properties
    * 自定义配置文件           （使用 -Dnorns.job.config=${path} 指定）
    */
  private val defaultLoadConfig: NornsConfig = {
    val config = NornsConfig.load(applicationConfig = Option(System.getProperty(nornsJobConfig)))
    info(s"defaultLoadConfig:${config.show}")
    config
  }
}