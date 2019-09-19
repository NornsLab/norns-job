package io.github.nornslab.norns.examples.spark

import io.github.nornslab.norns.core.NornsJob
import io.github.nornslab.norns.core.api.Task
import io.github.nornslab.norns.spark._


object SparkTaskJobExample {
  def main(args: Array[String]): Unit = NornsJob.work(classOf[SparkTaskJobExample])
}

class SparkTaskJobExample extends SparkTaskJob {

  override def runningTasks(data: Map[String, AnyRef]): Seq[Task] = Seq(new SparkTaskNumber(context, data))

}

class SparkTaskNumber(override val context: SJC,
                      override val data: Map[String, AnyRef])
  extends SparkTask(context, data) {
  override def start(): Unit = {
    context.sparkContext.parallelize(1 to 10).foreach(println(_))
  }
}

