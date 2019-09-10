package io.github.nornslab.norns

import io.github.nornslab.norns.core._

/**
  * =工作运行模式支持=
  * [[Service]]   ：服务接口封装，提供一个服务 init->start-stop 流程操作
  * [[Job]]       ：工作，作为 norns-job设计核心类，一个进程（main方法）启动一个工作
  * [[Task]]      ：任务，任务只能由 [[Job]] 进行管理，不支持单独启动一个 task
  * [[PluginTask]]：插件式任务，任务按照 input->filters->outputs 流式处理
  * [[TaskJob]]   ：多子任务[[Task]]组合为一个工作运行
  *
  * =[[Context]]上下文环境说明=
  * [[Job]]       运行时依赖参数封装为[[io.github.nornslab.norns.core.JobContext]]，同时默认装载配置信息
  * [[Task]]      运行时依赖参数封装为二元组 ([[io.github.nornslab.norns.core.JobContext]],Config) Config为每个task实例单独依赖配置信息封装
  *
  * =[[TaskJob]]=
  * 对于[[TaskJob]]模式任务，支持将 [[io.github.nornslab.norns.core.JobContext]]转换为多个(JobContext,Config)，每个依赖执行一次
  * 配置信息参考 https://github.com/lightbend/config/blob/master/HOCON.md
  *
  * =[[TaskPlugin]]=
  *
  * =工作启动=
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
package object core {

  val nornsJob = s"norns.core.job"

  /** 读取 job 配置文件路径 */
  val nornsJobConfig = s"norns.job.config"

  /** 默认载入 job 配置文件 */
  val nornsJobConf = "norns-job.conf"
  val nornsJobJson = "norns-job.json"
  val nornsJobProperties = "norns-job.properties"

  /** [[TaskJob ]]运行子任务类名配置 */
  val multipleTasks = "multipleTasks"
  val taskClassName = "taskClassName"

  val input = "input"
  val filter = "filter"
  val output = "output"

  val plugin = "plugin"

}
