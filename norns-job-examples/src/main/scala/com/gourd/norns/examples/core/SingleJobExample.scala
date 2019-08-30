package com.gourd.norns.examples.core

import com.gourd.norns.core.{JobContext, NornsMain, SingleJob}

/**
  * @author Li.Wei by 2019/8/30
  */
object SingleJobExample {
  def main(args: Array[String]): Unit = NornsMain.work(classOf[SingleJobExample])
}

class SingleJobExample extends SingleJob {

  override type JC = SingleJobExampleContext

  override def jc: SingleJobExampleContext = new SingleJobExampleContext

  /** job 运行 */
  override def run(): Unit = {
    info(s"$name running...")
    info(s"$name running context.foo=${jc.foo}...")
  }
}

class SingleJobExampleContext extends JobContext {
  def foo: String = "foo"
}