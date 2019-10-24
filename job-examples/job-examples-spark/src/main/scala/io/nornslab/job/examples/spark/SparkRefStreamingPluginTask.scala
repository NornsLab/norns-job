package io.nornslab.job.examples.spark

import io.nornslab.job.core.NornsJob
import io.nornslab.job.spark.SparkStructuredStreamingTaskJob

/**
  * @author Li.Wei by 2019/10/23
  */
object SparkRefStreamingPluginTask {
  def main(args: Array[String]): Unit = {
    System.setProperty("norns.job.config",
      ClassLoader.getSystemResource("spark/SparkRefStreamingPluginTask.conf").getPath)

    NornsJob.work(classOf[SparkStructuredStreamingTaskJob])
  }
}
