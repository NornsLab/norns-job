package io.github.nornslab.norns.core.api

/** 工作
  *
  * @author Li.Wei by 2019/8/29
  */
trait Job extends LifecycleAware {

  type JC <: JobContext

  def context: JC

  override def close(): Unit = context.close()
}
