package io.github.nornslab.norns.core.api

/** Output
  *
  * @tparam E 插件处理流程依赖数据结构
  */
trait Output[E] {

  def output(event: E): Unit

}
