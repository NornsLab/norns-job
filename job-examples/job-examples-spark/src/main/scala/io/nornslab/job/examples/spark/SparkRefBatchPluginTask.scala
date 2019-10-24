package io.nornslab.job.examples.spark

import io.nornslab.job.core.NornsJob
import io.nornslab.job.core.api.TaskContext
import io.nornslab.job.spark.{SJC, SparkBatchTaskJob, SparkTask}

/**
  * @author Li.Wei by 2019/8/30
  */
object SparkRefPluginTaskExample {
  def main(args: Array[String]): Unit = {
    System.setProperty("norns.job.config",
      ClassLoader.getSystemResource("spark/SparkRefBatchPluginTask.conf").getPath)

    NornsJob.work(classOf[SparkBatchTaskJob])
  }
}

class NewUser(implicit override val jc: SJC,
              implicit override val tc: TaskContext) extends SparkTask {
  override def start(): Unit = {
    info(s"$name running...")
  }
}

class NewRole(implicit override val jc: SJC,
              implicit override val tc: TaskContext) extends SparkTask {
  override def start(): Unit = {
    info(s"$name running...")
  }
}