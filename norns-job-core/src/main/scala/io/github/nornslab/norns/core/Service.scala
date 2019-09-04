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

  /** [[start()]] 执行前 初始化，打开资源等操作 */
  def init(): this.type = this

  /** job 运行 */
  def start(): Unit

  /** job 停止，默认调用 close 方法 */
  def stop(): Unit = close()

  override def close(): Unit = context.close()

}
