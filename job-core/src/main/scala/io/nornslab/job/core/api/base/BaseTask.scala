package io.nornslab.job.core.api.base

import io.nornslab.job.core.api.{JobContext, Task, TaskContext}

/** Task 任务基础类
  *
  * =说明=
  * 请勿在 task 中关闭 context , context 关闭默认由 job 管理
  *
  * @param jc JobContext
  * @param tc TaskContext
  * @tparam JC JobContext type
  * @author Li.Wei by 2019/9/2
  */
abstract class BaseTask[JC <: JobContext](implicit val jc: JC,
                                          implicit val tc: TaskContext = TaskContext.empty)
  extends Task

