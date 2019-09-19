package io.github.nornslab.norns.examples.spark

import io.github.nornslab.norns.core.NornsJob
import io.github.nornslab.norns.spark.{SJC, SparkTask, SparkTaskJob}

/**
  * @author Li.Wei by 2019/8/30
  */
object SparkRefPluginTaskExample {
  def main(args: Array[String]): Unit = {
    System.setProperty("norns.job.config",
      ClassLoader.getSystemResource("norns-taskJob-multipleTasks-examples.conf").getPath)

    NornsJob.work(classOf[SparkTaskJob])
  }
}

class NewUser(override val context: SJC,
              override val data: Map[String, AnyRef])
  extends SparkTask(context, data) {
  override def start(): Unit = {
    info(s"$name running...")
  }
}

class NewRole(override val context: SJC,
              override val data: Map[String, AnyRef])
  extends SparkTask(context, data) {
  override def start(): Unit = {
    info(s"$name running...")
  }
}