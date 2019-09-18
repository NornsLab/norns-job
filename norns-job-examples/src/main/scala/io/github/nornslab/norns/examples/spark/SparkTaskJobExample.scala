package io.github.nornslab.norns.examples.spark

import com.typesafe.config.Config
import io.github.nornslab.norns.core.NornsJob
import io.github.nornslab.norns.core.api.Task
import io.github.nornslab.norns.spark._


object SparkTaskJobExample {
  def main(args: Array[String]): Unit = NornsJob.work(classOf[SparkTaskJobExample])
}

class SparkTaskJobExample extends SparkTaskJob {

  override def runningTasks(implicit tc: (C, Config)): Seq[Task] = Seq(new SparkTaskNumber())

}

class SparkTaskNumber(override implicit val tc: (SJC, Config)) extends SparkTask {
  override def start(): Unit = {
    context.sparkContext.parallelize(1 to 10).foreach(println(_))
  }
}

