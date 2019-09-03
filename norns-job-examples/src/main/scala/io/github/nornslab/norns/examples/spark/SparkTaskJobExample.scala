package io.github.nornslab.norns.examples.spark

import io.github.nornslab.norns.spark.SparkJobContext._
import io.github.nornslab.norns.spark._

class SparkTaskJobExample(private val _stc: STC) extends SparkTaskJob {

  private implicit val context: STC = SparkTaskContext(jc)

  override def tasks: Seq[SparkTask] =
    Seq(
      new SparkTask() {
        override def run(): Unit = {
          // code...
        }
      }
    )
}

class SparkTaskNumber(implicit override val tc: STC) extends SparkTask {

  implicit def im(stc: STC): SJC = stc.jc

  // implicit tc: SparkTaskContext
  override def run(): Unit = {
    sql("")
  }

}

