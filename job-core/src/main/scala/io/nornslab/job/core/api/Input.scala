package io.nornslab.job.core.api

/** Input  插件，作为数据生产者
  *
  * @tparam E 插件处理流程依赖数据结构
  */
trait Input[E] extends Plugin {

  def input: E

}
