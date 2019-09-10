package io.github.nornslab.norns.core

import io.github.nornslab.norns.core.utils.Logging

import scala.util.{Failure, Success, Try}

/** 定义服务流程 init -> start -> stop
  *
  * 服务提供上下文环境 context
  *
  * @author Li.Wei by 2019/9/4
  */
trait Service extends Logging with AutoCloseable {

  type C <: Context

  def name: String = getClass.getName

  def context: C

  /** 启动前初始化操作，参数校验、资源配置信息初始化等操作 */
  def init: Try[this.type] = Try(this)

  /** 启动服务运行处理逻辑或者资源初始化等操作 */
  def start(): Unit = {}

  /** 停止，默认调用 close 方法 */
  def stop(): Unit = close()

  /** 关闭资源 */
  override def close(): Unit = {}

  /**
    * 快速启动，封装了启动中服务流程的执行过程
    * {{{
    *   Service s = new Service()
    *   s.fastExecute()
    * }}}
    */
  def fastExecute(): this.type = {
    try {
      /* init match {
        case Left(e) => error(s"fastStart error : e=${e.getMessage}"); throw e
        case Right(t) => t.start()
      } */
      init match {
        case Failure(exception) => error(s"fastStart error : e=${exception.getMessage}"); exception.printStackTrace()
        case Success(service) => service.start()
      }
    } finally {
      stop()
    }
    this
  }

}
