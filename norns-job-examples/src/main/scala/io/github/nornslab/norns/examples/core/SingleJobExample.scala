package io.github.nornslab.norns.examples.core

import io.github.nornslab.norns.core.{Job, JobContext, NornsMain}
/**
  * @author Li.Wei by 2019/8/30
  */
object SingleJobExample {
  def main(args: Array[String]): Unit = NornsMain.work(classOf[SingleJobExample])
}

class SingleJobExample extends Job {

  override type JC = SingleJobExampleContext

  override def jc: SingleJobExampleContext = new SingleJobExampleContext

  /** job 运行 */
  override def run(): Unit = info(s"$name running... context.foo=${jc.foo}...")
}

class SingleJobExampleContext extends JobContext {
  def foo: String = "foo"
}