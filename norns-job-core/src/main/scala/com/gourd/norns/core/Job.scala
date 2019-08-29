package com.gourd.norns.core

import com.gourd.norns.core.Constant._
import com.gourd.norns.core.utils.Logging
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory._

/**
  * @author Li.Wei by 2019/8/29
  */
trait Job extends Logging {

  type C = Context

  /** 任务名称 */
  def name: String = getClass.getCanonicalName

  /**
    * 为简化配置操作，不引用 main 函数传入 args参数，推荐使用系统参数（-D）或者配置文件
    * =配置装载顺序=
    * 系统环境变量 env
    * 系统参数 -D
    * 默认配置文件（非必须） defaultConfigConf
    * 默认配置文件（非必须） nornsConf
    * 自定义配置文件（推荐使用 -Dnorns.job.config=${path}指定）
    *
    * 装载后截取[[Constant.norns]]节点数据作为配置项
    */
  private lazy val _config: Config = {
    val sysConf = empty()
      .withFallback(systemEnvironment)
      .withFallback(systemProperties)

    sysConf
      .withFallback(loadConfFile(None -> defaultConfigConf))
      .withFallback(loadConfFile(None -> nornsConf))
      .withFallback(loadConfFile(Some(sysConf) -> jobConfig))
      .withOnlyPath(norns)
  }

  def config: Config = _config

  /** [[run()]] 运行前打开资源 */
  def open(): Unit = {
    info(config.root().render(renderOptions))
  }

  def context: C

  /** job 运行 */
  def run(): Unit

}


