package io.github.nornslab.norns.core.api.base

import io.github.nornslab.norns.core.api.TaskContext

/**
  * @author Li.Wei by 2019/9/19
  */
case class EmptyTaskContext() extends TaskContext

object EmptyTaskContext {
  val empty = EmptyTaskContext()
}