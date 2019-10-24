package io.nornslab.job.core.api

/** 工作，所有任务的均有 job 进行管理
  *
  * 工作模式
  * 1. job 运行为单个自定义实现逻辑任务
  * 2. job 运行为由多个 [[Task]] 组合为列表顺序执行[[TaskJob]]
  *
  * @tparam JC JobContext
  * @author Li.Wei by 2019/8/29
  */
trait Job[JC <: JobContext] extends LifecycleAware {

  implicit def context: JC

  override def close(): Unit = context.close()
}
