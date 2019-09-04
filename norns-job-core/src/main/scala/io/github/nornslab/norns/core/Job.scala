package io.github.nornslab.norns.core

/** 工作
  *
  * =工作运行模式支持=
  * [[Job]]       ：单个工作运行
  * [[TaskJob]]   ：多子任务[[Task]]组合为一个工作运行
  *
  * =[[Context]]上下文环境说明=
  * [[Job]]       运行时依赖参数封装为[[JobContext]]，同时默认装载配置信息[[JobContext.config]]
  * [[Task]]      运行时依赖参数封装为二元组 ([[JobContext]],Config) Config为每个task实例单独依赖配置信息封装
  *
  * =[[TaskJob]]=
  * 对于[[TaskJob]]模式任务，支持将 [[JobContext]]转换为多个([[JobContext]],Config)，每个依赖执行一次
  * 配置信息参考 https://github.com/lightbend/config/blob/master/HOCON.md
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
trait Job extends Service {

  override type C <: JobContext

}













