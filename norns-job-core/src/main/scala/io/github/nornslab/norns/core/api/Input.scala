package io.github.nornslab.norns.core.api

/** Input
  *
  * @tparam E 插件处理流程依赖数据结构
  */
trait Input[E] extends Plugin {

  def input: E

}
