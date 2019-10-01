package io.github.nornslab.norns.core.api.base

import io.github.nornslab.norns.core.api.JobContext

/**
  * @author Li.Wei by 2019/9/19
  */
case class DefaultJobContext() extends JobContext

object DefaultJobContext {
  val empty = DefaultJobContext()
}