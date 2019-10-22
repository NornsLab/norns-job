package io.github.nornslab.norns.examples.core

import io.github.nornslab.norns.core.NornsJob
import io.github.nornslab.norns.core.api.base.{BaseTask, DefaultJobContext, DefaultTaskJob}
import io.github.nornslab.norns.core.api.{NornsConfig, Task, TaskContext}
import io.github.nornslab.norns.core.utils.Logging


object TaskJobExample extends Logging {

  def main(args: Array[String]): Unit = NornsJob.work(classOf[TaskJobExample])
}

class TaskJobExample extends DefaultTaskJob {

  override def buildTasks(jc: DefaultJobContext)(implicit tc: TaskContext): Seq[Task] =
    Seq(new BaseAppTask1(), new BaseAppTask2())

  override def buildTaskContexts: Seq[TaskContext] = Seq(
    TaskContext(NornsConfig.loadFrom(Map("app" -> "app1"))),
    TaskContext(NornsConfig.loadFrom(Map("app" -> "app2")))
  )
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
