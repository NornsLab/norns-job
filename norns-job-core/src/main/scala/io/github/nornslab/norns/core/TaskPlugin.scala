package io.github.nornslab.norns.core

/** 任务插件
  *
  * D 插件处理流程依赖数据结构
  *
  * @author Li.Wei by 2019/9/2
  */
trait TaskPlugin extends Service

trait Input[D] extends TaskPlugin {
  def input(): D
}

trait Filter[D] extends TaskPlugin {
  def filter(d: D): D
}

trait Output[D] extends TaskPlugin {
  def output(d: D): Unit
}

