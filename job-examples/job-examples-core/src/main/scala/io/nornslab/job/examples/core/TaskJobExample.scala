package io.nornslab.job.examples.core

import io.nornslab.job.core.NornsJob
import io.nornslab.job.core.api.base.{BaseTask, BaseTaskBuilder}
import io.nornslab.job.core.api.{NornsConfig, Task, TaskContext}
import io.nornslab.job.core.utils.Logging


object TaskJobExample extends Logging {

  def main(args: Array[String]): Unit = NornsJob.work(classOf[TaskJobExample])
}

class TaskJobExample extends DefaultTaskJob {

  override val taskBuilderImpl = new BaseTaskBuilder[DefaultJobContext, Serializable] {
    override def buildTasks(jc: DefaultJobContext)(implicit tc: TaskContext): Seq[Task] =
      Seq(new BaseAppTask1(), new BaseAppTask2())

    override def buildTaskContexts: Seq[TaskContext] = Seq(
      TaskContext(NornsConfig.loadFrom(Map("app" -> "app1"))),
      TaskContext(NornsConfig.loadFrom(Map("app" -> "app2")))
    )
  }
}

class BaseAppTask1(implicit override val jc: DefaultJobContext,
                   implicit override val tc: TaskContext)
  extends BaseTask[DefaultJobContext] {

  override def start(): Unit = {
    info(s"""$name . running code by app=${tc.nornsConfig.get[String]("app")}""")
  }

}

class BaseAppTask2(implicit override val jc: DefaultJobContext,
                   implicit override val tc: TaskContext)
  extends BaseTask[DefaultJobContext] {

  override def start(): Unit = {
    info(s"""$name . running code by app=${tc.nornsConfig.get[String]("app")}""")
  }

}
