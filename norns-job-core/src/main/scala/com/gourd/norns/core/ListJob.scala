package com.gourd.norns.core

/**
  * @author Li.Wei by 2019/8/29
  */
trait ListJob extends Job {

  def jobs: Seq[Job] = listJob ++ refJob // 自定义 + 反射

  def listJob: Seq[Job]

  private def refJob: Seq[Job] = Seq()

  /** job 运行 */
  override def run(): Unit = listJob.foreach(_.run())

}

