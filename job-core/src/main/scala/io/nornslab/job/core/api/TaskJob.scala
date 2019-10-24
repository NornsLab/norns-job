package io.nornslab.job.core.api

/** 组合多个 task 为一个列表交于 job 进行运行 ， 具体 task 列表组合方式由 [[.taskBuilder]] 控制
  *
  * @author Li.Wei by 2019/10/24
  */
trait TaskJob[JC <: JobContext] extends Job[JC] {

  def taskBuilder: TaskBuilder[JC]

}
