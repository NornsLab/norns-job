package io.github.nornslab.norns.core

/** 工作
  *
  * @author Li.Wei by 2019/8/29
  */
trait Job extends Service {

  override type C <: JobContext

  override def close(): Unit = context.close()
}













