package com.gourd.norns.core.example

import com.gourd.norns.core.utils.Logging
import com.typesafe.config.ConfigFactory

/**
  * @author Li.Wei by 2019/8/29
  */
object App extends Logging {

  def main(args: Array[String]): Unit = {
    println("~~~")
    // ConfigFactory.load().getConfig().
    info("~~~~~")

    new TestJob().config

    val config = ConfigFactory.parseResources("norns.conf")
    info(config.root().render())

  }

}
