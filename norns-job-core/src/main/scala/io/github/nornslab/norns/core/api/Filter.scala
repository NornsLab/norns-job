package io.github.nornslab.norns.core.api

/** Filter
  *
  * @tparam E 插件处理流程依赖数据结构
  */
trait Filter[E] extends Plugin {

  def filter(event: E): E

}
