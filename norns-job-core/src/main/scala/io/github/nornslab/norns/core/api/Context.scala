package io.github.nornslab.norns.core.api

import com.typesafe.config.Config
import io.github.nornslab.norns.core.utils.Logging

/** 上下文内容
  *
  * @author Li.Wei by 2019/8/29
  */
trait Context extends Logging with AutoCloseable {

  def name: String = this.getClass.getCanonicalName

  /** 当前上下文内容提供配置信息 */
  def config: Config

  override def close(): Unit = {}

}
