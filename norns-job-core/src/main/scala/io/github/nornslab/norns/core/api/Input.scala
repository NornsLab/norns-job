package io.github.nornslab.norns.core.api

/** Input
  *
  * @tparam TPD 插件处理流程依赖数据结构
  */
trait Input[TPD] extends Plugin {
  def input: TPD
}
