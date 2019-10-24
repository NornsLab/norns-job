package io.nornslab.job.core.api

import io.nornslab.job.core.utils.Logging

/** 定义生命周期流程 init -> start -> stop
  *
  * @author Li.Wei by 2019/9/4
  */
trait LifecycleAware extends Logging with NamedComponent with AutoCloseable {

  /** 启动前初始化操作，参数校验、资源配置信息初始化等操作 */
  def init: Option[Throwable] = None

  /** 启动服务运行处理逻辑或者资源初始化等操作 */
  def start(): Unit = {}

  /** 停止，默认调用 close 方法 */
  def stop(): Unit = close()

  /** 关闭资源 */
  override def close(): Unit = {}

  /** 快速执行，封装了启动中服务流程的执行过程 init -> start -> stop */
  def fastExecute(): Unit = {
    try {
      init match {
        case Some(exception) =>
          error(
            s"""fastExecute error msg=${exception.getMessage}
               |Suppressed=${exception.getSuppressed.map(_.getMessage).mkString("\n")}
               |""".stripMargin)
          exception.printStackTrace()
        case None => info("init succeed , service start")
          start()
      }
    } finally {
      stop()
    }
  }

  /** 快速启动，封装了启动中服务流程的执行过程 start -> stop */
  def fastStart(): Unit = {
    try {
      start()
    } finally {
      stop()
    }
  }

}
