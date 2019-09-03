package io.github.nornslab.norns.core

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory.empty

/**
  * @author Li.Wei by 2019/9/2
  */
trait TaskContext extends Context {

  /** 当前 task 所在 job 的上下文环境 */
  def jc: JobContext

  /** 当前 task 运行依赖配置 */
  def config: Config = empty()
}
