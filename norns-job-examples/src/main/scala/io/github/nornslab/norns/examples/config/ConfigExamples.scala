package io.github.nornslab.norns.examples.config

import java.util

import com.typesafe.config.{Config, ConfigFactory, ConfigResolveOptions}
import io.github.nornslab.norns.core.utils.Logging

import scala.collection.JavaConverters._

/**
  * @author Li.Wei by 2019/9/6
  */
object ConfigExamples extends Logging {

  def main(args: Array[String]): Unit = {
    val root = ConfigFactory
      .parseResources("norns-job-examples.conf")
      .resolve(ConfigResolveOptions.defaults().setAllowUnresolved(true))

    val str = root.getString("jobClassName")
    // root.getConfig("input").getAnyRefList("")
    val inputList = root.getAnyRefList("input")
    info(s"inputList = \n$inputList")


    val configList: util.List[_ <: Config] = root.getConfigList("tasks")
    info(s"configList = \n${configList}")

    configList.asScala.foreach((f: Config) => {
      info(s"$f")
      val input = f.getConfigList("input")
      info(s"input=>$input")
    })
  }
}
