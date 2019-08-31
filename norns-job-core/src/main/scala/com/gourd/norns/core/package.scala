package com.gourd.norns

/**
  * 设计原则参考 [[com.gourd.norns.core.Job]] 注释说明
  *
  * @author Li.Wei by 2019/8/29
  */
package object core {

  val nornsJob = s"${Constant.nornsCore}.core.job"

  /** 读取 job 配置文件路径 */
  val nornsJobConfig = s"${Constant.nornsCore}.core.jobConfig"

  /** 默认载入 job 配置文件 */
  val nornsJobConf = "norns-job.conf"
  val nornsJobJson = "norns-job.json"
  val nornsJobProperties = "norns-job.properties"

  /** [[MultiJob ]]运行子任务类名配置 */
  val jobRunTasks = s"${Constant.nornsCore}.core.jobRunTasks"

}
