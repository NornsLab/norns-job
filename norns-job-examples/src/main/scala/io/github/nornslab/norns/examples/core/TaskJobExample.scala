package io.github.nornslab.norns.examples.core

import io.github.nornslab.norns.core._
import io.github.nornslab.norns.core.api.base.{BaseTask, BaseTaskJob, EmptyJobContext}
import io.github.nornslab.norns.core.api.{JobContext, Task, TaskBuilder, TaskContext}
import io.github.nornslab.norns.core.utils.Logging

object TaskJobExample extends Logging {

  def main(args: Array[String]): Unit = NornsJob.work(classOf[TaskJobExample])
}

class TaskJobExample extends BaseTaskJob {

  override val taskBuilder: TaskBuilder = new TaskBuilder {
    override type TC = AppTaskContext

    override def buildTasks(jc: JobContext)(implicit tc: TC): Seq[Task] = Seq(BaseAppTask())

    override def buildTaskContexts: Seq[TC] = Seq(new AppTaskContext("app-1"), new AppTaskContext("app-2"))
  }

  override type JC = EmptyJobContext

  override def context: EmptyJobContext = EmptyJobContext.empty

}

class AppTaskContext(val app: String) extends TaskContext

case class BaseAppTask(implicit override val tc: AppTaskContext) extends BaseTask {

  override def start(): Unit = {
    info(s"$name . running code by app=${tc.app}")
  }

}