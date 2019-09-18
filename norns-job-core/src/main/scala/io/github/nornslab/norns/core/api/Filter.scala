package io.github.nornslab.norns.core.api

/** Filter
  *
  * @tparam TPD 插件处理流程依赖数据结构
  */
trait Filter[TPD] extends Plugin {
  def filter(d: TPD): TPD
}
