package io.github.nornslab.norns.core.api

/** 上下文内容
  *
  * @author Li.Wei by 2019/8/29
  */
trait Context extends AutoCloseable with NamedComponent {

  override def close(): Unit = {}
}


