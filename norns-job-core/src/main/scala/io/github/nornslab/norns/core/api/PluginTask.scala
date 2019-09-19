package io.github.nornslab.norns.core.api

/** 插件式 Task 任务
  * 多个 Plugin 返回需支持协变，默认为  Seq[Obj] 格式
  *
  * @tparam E 插件处理流程依赖数据结构
  */
trait PluginTask[E] extends Task {

  def input: Input[E]

  def filters: Seq[Filter[E]] = Seq.empty

  def outputs: Seq[Output[E]]
}
