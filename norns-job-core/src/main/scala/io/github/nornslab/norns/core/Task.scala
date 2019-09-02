package io.github.nornslab.norns.core

import io.github.nornslab.norns.core.utils.Logging

/**
  * @author Li.Wei by 2019/9/2
  */
trait Task[TC <: TaskContext] extends Logging {
  def name: String = getClass.getCanonicalName

  def run(tc: TC): Unit
}

trait PlugTask[TC <: TaskContext, D] extends Task[TC] {

  def inputPlug: Input[TC, D]

  def filterPlugs(): Array[Filter[TC, D]] = Array.empty

  def outputPlugs(): Array[Output[TC, D]]

  override def run(tc: TC): Unit = {
    // 推导为链式写法 待测试 多输出情况下提供cache操作(可用filter实现，具体根据输出out是否为多个自行定义) 提供并行写出操作
    outputPlugs().foreach {
      _.output(tc, filterPlugs().foldLeft(inputPlug.input(tc))((d, f) => f.filter(tc, d)))
    }
  }
}