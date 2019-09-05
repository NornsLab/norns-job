package io.github.nornslab.norns.core

import io.github.nornslab.norns.core.utils.Logging

/** 定义服务流程 init -> start -> stop
  *
  * 服务提供上下文环境 context
  *
  * @author Li.Wei by 2019/9/4
  */
trait Service extends Logging with AutoCloseable {

  type C <: Context

  def name: String = getClass.getCanonicalName

  def context: C

  // todo 协变问题
  /** 启动前初始化操作，参数校验、资源配置信息初始化等操作 */
  def init: Either[Throwable, this.type] = Right(this)

  /** 启动服务运行处理逻辑或者资源初始化等操作 */
  def start(): Unit = {}

  /** 停止，默认调用 close 方法 */
  def stop(): Unit = close()

  /** 关闭资源 */
  override def close(): Unit = context.close()

  /**
    * 快速启动，封装了启动中服务流程的执行过程
    * {{{
    *   Service s = new Service()
    *   s.fastStart()
    * }}}
    */
  def fastStart(): this.type = {
    try {
      init match {
        case Left(e) => error(s"fastStart error : e=${e.getMessage}"); throw e
        case Right(t) => t.start()
      }
    } finally {
      stop()
    }
    this
  }

}
