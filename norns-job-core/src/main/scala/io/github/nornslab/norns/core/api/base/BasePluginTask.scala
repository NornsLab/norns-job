package io.github.nornslab.norns.core.api.base

import io.github.nornslab.norns.core.api.PluginTask

/** 提供 Task 插件式任务类
  *
  * @tparam E 插件处理流程依赖数据结构
  * @author Li.Wei by 2019/9/2
  */
abstract class BasePluginTask[E] extends PluginTask[E] {
  // self =>

  /* override def init: Option[Throwable] =
     (Seq(self.input.init) ++ self.filters.map(_.init) ++ self.outputs.map(_.init))
       .filter(_.isDefined)
       .map(_.get)
       .reduceOption((e1: Throwable, e2: Throwable) => {
         e1.addSuppressed(e2)
         e1
       })
 */
  override def start(): Unit = {
    // 推导为链式写法 待测试 多输出情况下提供cache操作(可用filter实现，具体根据输出out是否为多个自行定义) 提供并行写出操作
    outputs.foreach {
      _.output(filters.foldLeft(input.input)((d, f) => f.filter(d)))
    }
  }

}
