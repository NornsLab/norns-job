package io.nornslab.job.core.api

/** Filter 插件
  *
  * @tparam E 插件处理流程依赖数据结构
  */
trait Filter[E] extends Plugin {

  def filter(event: E): E

}
