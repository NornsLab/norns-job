package com.gourd.norns.core

import com.gourd.norns.core.utils.Logging

/** 上下文内容
  *
  * @author Li.Wei by 2019/8/29
  */
trait Context extends Logging with AutoCloseable {
  def name: String = this.getClass.getCanonicalName

  override def close(): Unit = {}
}
