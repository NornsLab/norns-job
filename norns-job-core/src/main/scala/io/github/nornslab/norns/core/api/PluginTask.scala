package io.github.nornslab.norns.core.api

/** 插件式 Task 任务
  * 多个 Plugin 返回需支持协变，默认为  Seq[Obj] 格式
  *
  * @tparam PLUG_EVENT 插件处理流程依赖数据结构
  */
trait PluginTask[PLUG_EVENT <: Serializable] extends Task {

  def input: Input[PLUG_EVENT]

  def filters: Seq[Filter[PLUG_EVENT]] = Seq.empty

  def outputs: Seq[Output[PLUG_EVENT]]
}
