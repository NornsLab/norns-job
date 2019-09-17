package io.github.nornslab.norns.core

import com.typesafe.config.Config

/** 任务
  *
  * C = Task 依赖当前 job 上下文环境
  *
  * =说明=
  * 请勿在 task 中关闭 context , context 关闭默认由 job 管理
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

  val dataConfig: Config = tc._2

  implicit override def context: C = tc._1

  override def start(): Unit = {}
}

/** 插件式 Task 任务
  * 多个 Plugin 返回需支持协变，默认为  Seq[Obj] 格式
  *
  * @tparam PDT 插件处理流程依赖数据结构
  */
trait PluginTask[PDT] extends Task {

  def input: Input[PDT]

  def filters: Seq[Filter[PDT]] = Seq.empty

  def outputs: Seq[Output[PDT]]
}


/** 提供 Task 插件式任务类
  *
  * @param tc Task 依赖当前 job 上下文环境及配置信息
  * @tparam JC  Task 依赖当前 job 上下文环境
  * @tparam PDT 插件处理流程依赖数据结构
  */
abstract class BasePluginTask[JC <: JobContext, PDT](implicit override val tc: (JC, Config))
  extends BaseTask[JC]
    with PluginTask[PDT] {
  self =>

  /** 启动前初始化操作，参数校验、资源配置信息初始化等操作 */
  override def init: Option[Throwable] =
    (Seq(self.input.init) ++ self.filters.map(_.init) ++ self.outputs.map(_.init))
      .filter(_.isDefined)
      .map(_.get)
      .reduceOption((e1: Throwable, e2: Throwable) => {
        e1.addSuppressed(e2)
        e1
      })

  override def start(): Unit = {
    // 推导为链式写法 待测试 多输出情况下提供cache操作(可用filter实现，具体根据输出out是否为多个自行定义) 提供并行写出操作
    outputs.foreach {
      _.output(filters.foldLeft(input.input)((d, f) => f.filter(d)))
    }
  }

}