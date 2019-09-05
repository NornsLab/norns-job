package io.github.nornslab.norns.core

import com.typesafe.config.Config

/** 任务
  *
  * C = Task 依赖当前 job 上下文环境
  *
  * @author Li.Wei by 2019/9/2
  */
trait Task extends Service {
  override type C <: JobContext
}

/** Task 任务基础类
  *
  * @param tc Task 依赖当前 job 上下文环境及配置信息
  * @tparam JC Task 依赖当前 job 上下文环境
  */
class BaseTask[JC <: JobContext](implicit val tc: (JC, Config))
  extends Task {
  override type C = JC

  val taskConfig: Config = tc._2

  implicit override def context: C = tc._1

  override def start(): Unit = {}
}

/** 插件式 Task 任务
  * 多个 Plugin 返回需支持协变，默认为  Seq[Obj] 格式
  *
  * @tparam D 插件处理流程依赖数据结构
  */
trait PluginTask[D] extends Task {

  def input: Input[D]

  def filters(): Seq[Filter[D]] = Seq.empty

  def outputs(): Seq[Output[D]]
}


/** 提供 Task 插件式任务类
  *
  * @param tc Task 依赖当前 job 上下文环境及配置信息
  * @tparam JC Task 依赖当前 job 上下文环境
  * @tparam D  插件处理流程依赖数据结构
  */
abstract class BasePluginTask[JC <: JobContext, D](implicit override val tc: (JC, Config))
  extends BaseTask[JC]
    with PluginTask[D] {

  override def start(): Unit = {
    // 推导为链式写法 待测试 多输出情况下提供cache操作(可用filter实现，具体根据输出out是否为多个自行定义) 提供并行写出操作
    outputs().foreach {
      _.output(filters().foldLeft(input.input())((d, f) => f.filter(d)))
    }
  }

}