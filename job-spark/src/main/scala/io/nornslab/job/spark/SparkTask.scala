package io.nornslab.job.spark

import io.nornslab.job.core.api.TaskContext
import io.nornslab.job.core.api.base.BaseTask

/**
  * @author Li.Wei by 2019/9/3
  */
class SparkTask(implicit override val jc: SJC,
                implicit override val tc: TaskContext)
  extends BaseTask[SJC]





