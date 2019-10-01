package io.github.nornslab.norns.core.api.base

import io.github.nornslab.norns.core.api._

/**
  * TaskJob 默认实现
  *
  * @tparam JC         JobContext
  * @tparam PLUG_EVENT 插件task处理数据类型
  * @author Li.Wei by 2019/9/2
  */
abstract class BaseTaskJob[JC <: JobContext, PLUG_EVENT <: Serializable]
  extends BaseTaskBuilder[JC, PLUG_EVENT] with Job[JC] {

  // 待执行 task
  private[this] lazy val tasks: Seq[Task] = {
    val tss = buildTaskContexts
    info(s"taskData size [${tss.size}]")
    info(s"build tasks...")
    tss.flatten(buildTasks(context)(_))
  }

  /** 对所有待执行的 task 执行校验，默认调用每个 task 自身的 init 方法 */
  override def init: Option[Throwable] = tasks.map(_.init).filter(_.isDefined).map(_.get)
    .reduceOption((e1: Throwable, e2: Throwable) => {
      e1.addSuppressed(e2)
      e1
    })

  /**
    * 组合运行逻辑可推导为 for 写法，但是由于 for 中延迟初始化问题，不能在初始化时快速失败退出
    * {{{
    *   val runTasks: Seq[Task] = for {
    *       data <- tss
    *       t <- runningTasks(data)
    *     } yield t
    *     runTasks.foreach(_.fastStart())
    * }}}
    */
  override def start(): Unit = {
    info(s"tasks size [${tasks.size}] , ${tasks.map(_.name)}")

    tasks.foreach(t => {
      info(s"ready for operation task [$t]")
      t.fastStart()
    })
  }
}