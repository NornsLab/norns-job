package io.nornslab.job.core.api

/**
  * @author Li.Wei by 2019/9/20
  */
object NornsConfigTest {

  def main(args: Array[String]): Unit = {
    val config = NornsConfig.load()
    println(config.show)
  }
}
