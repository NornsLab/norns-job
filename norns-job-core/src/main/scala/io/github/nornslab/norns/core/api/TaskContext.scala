package io.github.nornslab.norns.core.api

/**
  *
  * @param nornsConfig TaskContext 中提供数据内容，目前设计为[[NornsConfig]] 作为数据传输对象
  * @author Li.Wei by 2019/9/30
  */
case class TaskContext(nornsConfig: NornsConfig)

object TaskContext {
  val empty = TaskContext(NornsConfig.loadEmpty)
}