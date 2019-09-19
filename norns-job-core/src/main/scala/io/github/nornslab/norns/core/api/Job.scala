package io.github.nornslab.norns.core.api

/** 工作
  *
  * @author Li.Wei by 2019/8/29
  */
trait Job extends Service {

  type C <: Context

  def context: C

  override def close(): Unit = context.close()
}
