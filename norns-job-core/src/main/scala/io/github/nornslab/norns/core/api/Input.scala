package io.github.nornslab.norns.core.api

import io.github.nornslab.norns.core.api.base.EmptyJobContext

/** Input
  *
  * @tparam E 插件处理流程依赖数据结构
  */
trait Input[E] {

  def input: E

}


trait Input1[JC <: JobContext, E] {

  def input(jc: JC): E

}

class Input1Impl extends Input1[EmptyJobContext, Int] {

  override def input(jc: EmptyJobContext): Int = {
    1
  }

}