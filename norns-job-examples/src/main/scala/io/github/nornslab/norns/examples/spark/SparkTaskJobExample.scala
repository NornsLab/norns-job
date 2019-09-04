package io.github.nornslab.norns.examples.spark

import com.typesafe.config.{Config, ConfigFactory}
import io.github.nornslab.norns.core.NornsMain
import io.github.nornslab.norns.spark._


object SparkTaskJobExample {
  def main(args: Array[String]): Unit = NornsMain.work(classOf[SparkTaskJobExample])
}

class SparkTaskJobExample extends SparkTaskJob {

  def tasks: Seq[Class[_]] = Seq(
    classOf[SparkTaskNumber], classOf[SparkTaskNumber]
  )

  override def contextConvert: C => Seq[(C, Config)] = sj => Seq(sj -> ConfigFactory.empty)

}

class SparkTaskNumber(private implicit val _tc: (SJC, Config)) extends SparkTask {
  override def start(): Unit = {
    context.sparkContext.parallelize(1 to 10).foreach(println(_))
  }
}

