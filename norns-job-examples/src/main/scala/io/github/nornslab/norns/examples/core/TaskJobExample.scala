package io.github.nornslab.norns.examples.core

import io.github.nornslab.norns.core._
import io.github.nornslab.norns.core.utils.Logging

object TaskJobExample extends Logging {

  def main(args: Array[String]): Unit = NornsJob.work(classOf[TaskJobExample])
}

class TaskJobExample extends BaseTaskJob {
  def loadApps: Seq[String] = Seq("1", "2", "3")

  override type C = EmptyJobContext

  override def context: C = EmptyJobContext()
}
