package io.nornslab.job.core.api.base

import io.nornslab.job.core.api.PluginTask

/** 提供 Task 插件式任务类
  *
  * @tparam PLUG_EVENT 插件处理流程依赖数据结构
  * @author Li.Wei by 2019/9/2
  */
abstract class BasePluginTask[PLUG_EVENT <: Serializable] extends PluginTask[PLUG_EVENT] {

  lazy val allPlug = outputs ++ filters ++ Seq(input)

  override def start(): Unit = {
    // 推导为链式写法 待测试 多输出情况下提供cache操作(可用filter实现，具体根据输出out是否为多个自行定义) 提供并行写出操作
    outputs.foreach {
      _.output(filters.foldLeft(input.input)((d, f) => f.filter(d)))
    }

    allPlug.foreach(_.start())
  }

  override def close(): Unit = {
    super.close()
    allPlug.foreach(_.close())
  }
}
