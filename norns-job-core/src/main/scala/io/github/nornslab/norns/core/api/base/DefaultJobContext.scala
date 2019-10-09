package io.github.nornslab.norns.core.api.base

import io.github.nornslab.norns.core.api.JobContext

/** [[JobContext]] 默认实现
  *
  * @author Li.Wei by 2019/9/19
  */
class DefaultJobContext extends JobContext

object DefaultJobContext {
  val empty = new DefaultJobContext()
}