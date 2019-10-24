package io.nornslab.job.core.api

/** Output 插件，作为消费者
  *
  * @tparam E 插件处理流程依赖数据结构
  */
trait Output[E] extends Plugin {

  def output(event: E): Unit

}
