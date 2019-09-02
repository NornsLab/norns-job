package io.github.nornslab.norns.core

import com.typesafe.config.Config
import io.github.nornslab.norns.core.utils.Logging

/** 任务入口
  *
  * =任务运行模式支持=
  * [[Job]] ：单个任务运行
  * [[TaskJob]]  ：多子任务[[Task]]组合为一个任务运行
  *
  * =[[Context]]上下文环境说明=
  * [[Job]]       运行时依赖参数封装为[[JobContext]]，同时默认装载配置信息[[JobContext.config]]
  * [[Task]]      运行时依赖参数封装为[[TaskContext]]
  * 对于[[TaskJob]]模式任务，支持将 [[JobContext]]转换为多个[[Config]]，每个[[Task]]依赖[[Config]]执行一次
  * 配置信息参考 https://github.com/lightbend/config/blob/master/HOCON.md
  *
  * =任务启动=
  * 统一Main方法入口 [[NornsMain]]
  *
  * 简单示例参考 norns-job-examples 模块中 package com.gourd.norns.examples.core 内容
  *
  * =编码建议=
  * - scala 常用函数式编程，习惯了 def 定义容易，对于资源初始化变量定义为 def（函数），导致多次引用时多次初始化
  * 例如将job中 Job.jc 定义为函数后多次引用时出现多次初始化问题，因添加为私有变量后方法返回，必要时支持 lazy
  *
  * @author Li.Wei by 2019/8/29
  */
trait Job extends Logging with AutoCloseable {

  type JC <: JobContext

  /** 任务名称 */
  def name: String = getClass.getCanonicalName

  /** [[run()]] 执行前 初始化，打开资源等操作 */
  def initialize(): this.type = {
    info(jc.config.root().render(Constant.renderOptions))
    this
  }

  /** job 上下文参数 */
  def jc: JC

  /** job 运行 */
  def run(): Unit

  /** job 运行结束资源关闭 */
  override def close(): Unit = {
    try {
      jc.close()
    } catch {
      case e: Exception => error("jc.close error", e)
    }
  }
}













