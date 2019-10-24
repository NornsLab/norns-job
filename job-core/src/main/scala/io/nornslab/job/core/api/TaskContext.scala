package io.nornslab.job.core.api

/** [[Task]] 运行依赖上下文内容
  *
  * @param nornsConfig TaskContext 中提供数据内容，目前设计为 [[NornsConfig]] 作为数据传输对象
  * @author Li.Wei by 2019/9/30
  */
case class TaskContext(nornsConfig: NornsConfig)

object TaskContext {
  val empty = TaskContext(NornsConfig.loadEmpty)
}