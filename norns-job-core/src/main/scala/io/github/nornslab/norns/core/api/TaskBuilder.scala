package io.github.nornslab.norns.core.api

/** TaskJob
  *
  * =执行 task 逻辑=
  * 当前待执行 task 为 runningTasks
  * 当前待执行 taskContext 为 taskContexts
  * 即累计运行 task 为 runningTasks * taskContexts ，组合逻辑参考 start 方法
  *
  * @author Li.Wei by 2019/9/2
  */
trait TaskBuilder {

  type TC <: TaskContext

  /** 当前待运行 Task，如需指定具体需要执行实例，重写该方法即可 */
  def buildTasks(jc: JobContext)(implicit tc: TC): Seq[Task]

  /** job context 转换为多个 task 依赖上下文环境 ，每个 task 依赖上下文环境将被 task执行一次 */
  def buildTaskContexts: Seq[TC]

}
