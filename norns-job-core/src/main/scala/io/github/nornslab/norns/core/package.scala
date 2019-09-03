package io.github.nornslab.norns

/**
  * 设计原则参考 [[io.github.nornslab.norns.core.Job]] 注释说明
  *
  * @author Li.Wei by 2019/8/29
  */
package object core {

  val nornsJob = s"norns.core.job"

  /** 读取 job 配置文件路径 */
  val nornsJobConfig = s"norns.core.jobConfig"

  /** 默认载入 job 配置文件 */
  val nornsJobConf = "norns-job.conf"
  val nornsJobJson = "norns-job.json"
  val nornsJobProperties = "norns-job.properties"

  /** [[TaskJob ]]运行子任务类名配置 */
  val runTasks = "norns.runTasks"
  val className = "className"

  val input = "input"
  val filter = "filter"
  val output = "output"

}
