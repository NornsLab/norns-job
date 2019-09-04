package io.github.nornslab.norns.core

import com.typesafe.config.Config

/** 任务
  *
  * @tparam JC Task 依赖当前 job 上下文环境
  * @author Li.Wei by 2019/9/2
  */
trait Task[JC <: JobContext] extends Service {
  override type C = JC
}

/** 插件式 Task 任务
  *
  * @tparam JC Task 依赖当前 job 上下文环境
  * @tparam D  插件处理流程依赖数据结构
  */
trait PlugTask[JC <: JobContext, D] extends Task[JC] {
  // self: Task[JC] =>

  def inputPlug: Input[C, D]

  def filterPlugs(): Array[Filter[C, D]] = Array.empty

  def outputPlugs(): Array[Output[C, D]]

}

/** Task 任务基础类
  *
  * @param _tc Task 依赖当前 job 上下文环境及配置信息
  * @tparam JC Task 依赖当前 job 上下文环境
  */
class BaseTask[JC <: JobContext](private implicit val _tc: (JC, Config))
  extends Task[JC] {

  private implicit val _stc: C = _tc._1
  val taskConfig: Config = _tc._2

  override def context: C = _stc

  override def start(): Unit = {}
}

/** 提供 Task 插件式任务类
  *
  * @param _tc          Task 依赖当前 job 上下文环境及配置信息
  * @param _inputPlug   inputPlug
  * @param _filterPlugs filterPlugs
  * @param _outputPlugs outputPlugs
  * @tparam JC Task 依赖当前 job 上下文环境
  * @tparam D  插件处理流程依赖数据结构
  */
class BasePlugTask[JC <: JobContext, D](private implicit val _tc: (JC, Config),
                                        private implicit val _inputPlug: Input[JC, D],
                                        private implicit val _filterPlugs: Array[Filter[JC, D]],
                                        private implicit val _outputPlugs: Array[Output[JC, D]])
  extends BaseTask
    with PlugTask[JC, D] {

  override def inputPlug: Input[C, D] = _inputPlug

  override def filterPlugs(): Array[Filter[C, D]] = _filterPlugs

  override def outputPlugs(): Array[Output[C, D]] = _outputPlugs

  override def start(): Unit = {
    // 推导为链式写法 待测试 多输出情况下提供cache操作(可用filter实现，具体根据输出out是否为多个自行定义) 提供并行写出操作
    outputPlugs().foreach {
      _.output(context, filterPlugs().foldLeft(inputPlug.input(context))((d, f) => f.filter(context, d)))
    }
  }

}