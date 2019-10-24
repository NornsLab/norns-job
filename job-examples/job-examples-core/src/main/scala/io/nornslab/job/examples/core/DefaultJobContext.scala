package io.nornslab.job.examples.core

import io.nornslab.job.core.api.JobContext

/** [[JobContext]] 默认实现
  *
  * @author Li.Wei by 2019/9/19
  */
class DefaultJobContext extends JobContext

object DefaultJobContext {
  val empty = new DefaultJobContext()
}