package io.github.nornslab.norns.examples.spark

import io.github.nornslab.norns.core.NornsJob
import io.github.nornslab.norns.spark.SparkTaskJob

/**
  * @author Li.Wei by 2019/10/23
  */
object SparkRefStreamingPluginTask {
  def main(args: Array[String]): Unit = {
    System.setProperty("norns.job.config",
      ClassLoader.getSystemResource("spark/SparkRefStreamingPluginTask.conf").getPath)

    NornsJob.work(classOf[SparkTaskJob])
  }
}
