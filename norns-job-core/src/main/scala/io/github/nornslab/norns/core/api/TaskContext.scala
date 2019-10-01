package io.github.nornslab.norns.core.api

/**
  * @author Li.Wei by 2019/9/30
  */
case class TaskContext(nornsConfig: NornsConfig)

object TaskContext {
  val empty = TaskContext(NornsConfig.loadEmpty)
}