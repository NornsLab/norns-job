package com.gourd.norns

/**
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

  /** 读取 listJob 配置文件路径 */
  val listJobConfig = s"${Constant.nornsCore}.core.listJobConfig"
  /** [[ListJob ]]运行子任务类名配置 */
  val jobRunSubJobs = s"${Constant.nornsCore}.core.runSubJobList"

}
