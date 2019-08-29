package com.gourd.norns.core.example

import com.gourd.norns.core.utils.Logging

/**
  * @author Li.Wei by 2019/8/29
  */
object App extends Logging {

  def main(args: Array[String]): Unit = {
    // ConfigFactory.load().getConfig().
    new TestJob().open()
  }

}
