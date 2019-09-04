package io.github.nornslab.norns.core

import com.typesafe.config.Config
import io.github.nornslab.norns.core.utils.Logging

/** 上下文内容
  *
  * @author Li.Wei by 2019/8/29
  */
trait Context extends Logging with AutoCloseable {
  def name: String = this.getClass.getCanonicalName

  def config: Config

  override def close(): Unit = {}
}
