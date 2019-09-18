package io.github.nornslab.norns.core.api

/** Output
  *
  * @tparam TPD 插件处理流程依赖数据结构
  */
trait Output[TPD] extends Plugin {
  def output(d: TPD): Unit
}
