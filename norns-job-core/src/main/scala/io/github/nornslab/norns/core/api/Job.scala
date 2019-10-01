package io.github.nornslab.norns.core.api

/** 工作
  *
  * @tparam JC JobContext
  * @author Li.Wei by 2019/8/29
  */
trait Job[JC <: JobContext] extends LifecycleAware {

  implicit def context: JC

  override def close(): Unit = context.close()
}
