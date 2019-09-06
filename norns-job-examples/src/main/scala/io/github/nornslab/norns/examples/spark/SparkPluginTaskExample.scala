package io.github.nornslab.norns.examples.spark

import com.typesafe.config.Config
import io.github.nornslab.norns.core.{NornsMain, Task}
import io.github.nornslab.norns.spark.plugins.SparkInput
import io.github.nornslab.norns.spark.plugins.output.Stdout
import io.github.nornslab.norns.spark.{SJC, SparkPluginTask, SparkTaskJob}

/**
  * @author Li.Wei by 2019/8/30
  */
object SparkPluginTaskExample {
  def main(args: Array[String]): Unit = NornsMain.work(classOf[SparkPluginTaskExample])
}

class SparkPluginTaskExample extends SparkTaskJob {
  override def runningTasks(implicit tc: (C, Config)): Seq[Task] = Seq(new SparkFile())
}

class SparkFile(override implicit val tc: (SJC, Config)) extends SparkPluginTask {

  override def input: SparkInput = new SparkInput() {
    override def input() =
      context.sparkSession.read.text("file:///Users/liwei/Development/workspace/intellij/norns-job/" +
        "norns-job-examples/files/text.input.txt")
  }

  override def outputs(): Seq[Stdout] = Seq(new Stdout())
}