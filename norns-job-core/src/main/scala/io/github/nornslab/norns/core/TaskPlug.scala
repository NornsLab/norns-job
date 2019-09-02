package io.github.nornslab.norns.core

/** TaskPlug
  *
  * @tparam TC taskContext
  * @tparam D  插件运行处理数据流类型
  * @author Li.Wei by 2019/9/2
  */
trait TaskPlug[TC <: TaskContext, D]

trait Input[TC <: TaskContext, D] extends TaskPlug[TC, D] {
  def input(tc: TaskContext): D
}

trait Filter[TC <: TaskContext, D] extends TaskPlug[TC, D] {
  def filter(tc: TaskContext, d: D): D
}

trait Output[TC <: TaskContext, D] extends TaskPlug[TC, D] {
  def output(tc: TaskContext, d: D): Unit
}


///////////////////////////////////////////////////////////////////////////////////////////////////////
// FOR TEST
///////////////////////////////////////////////////////////////////////////////////////////////////////

class PlugTaskTest extends PlugTask[TaskContext, Int] {

  override def inputPlug: Input[TaskContext, Int] = new Input[TaskContext, Int] {
    override def input(c: TaskContext): Int = 1
  }

  override def filterPlugs(): Array[Filter[TaskContext, Int]] = Array(
    new Filter[TaskContext, Int] {
      override def filter(c: TaskContext, d: Int): Int = d - 1
    }, new Filter[TaskContext, Int] {
      override def filter(c: TaskContext, d: Int): Int = d + 1
    }, new Filter[TaskContext, Int] {
      override def filter(c: TaskContext, d: Int): Int = d + 1
    }
  )

  override def outputPlugs(): Array[Output[TaskContext, Int]] = Array(
    new Output[TaskContext, Int] {
      override def output(c: TaskContext, d: Int): Unit = println(s"out $d")
    }
  )
}

object PlugTaskTest {
  def main(args: Array[String]): Unit = {
    new PlugTaskTest().run(new TaskContext() {})
  }
}

