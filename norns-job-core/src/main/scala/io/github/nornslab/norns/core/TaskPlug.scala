package io.github.nornslab.norns.core

/** 任务插件
  *
  * @tparam C JobContext
  * @tparam D 插件处理流程依赖数据结构
  * @author Li.Wei by 2019/9/2
  */
trait TaskPlug[C <: JobContext, D]

trait Input[C <: JobContext, D] extends TaskPlug[C, D] {
  def input(tc: C): D
}

trait Filter[C <: JobContext, D] extends TaskPlug[C, D] {
  def filter(tc: C, d: D): D
}

trait Output[C <: JobContext, D] extends TaskPlug[C, D] {
  def output(tc: C, d: D): Unit
}

