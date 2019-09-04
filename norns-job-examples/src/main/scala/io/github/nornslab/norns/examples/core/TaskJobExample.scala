/*
package io.github.nornslab.norns.examples.core

import com.typesafe.config.{Config, ConfigFactory}
import io.github.nornslab.norns.core._
import io.github.nornslab.norns.core.utils.Logging
import io.github.nornslab.norns.examples.core.TaskJobExample.ExampleTask

import scala.collection.JavaConverters._

/**
  * @author Li.Wei by 2019/8/30
  */
object TaskJobExample extends Logging {
  type ExampleTask = Task[ExampleTaskContext]

  def main(args: Array[String]): Unit = NornsMain.work(classOf[TaskJobExample])
}

class ExampleJobContext extends JobContext {
  def loadApps: Seq[String] = Seq("1", "2", "3")
}

case class ExampleTaskContext(private val _jc: ExampleJobContext,
                              private val _config: Config)
  extends TaskContext {
  override def jc: ExampleJobContext = _jc

  override def config: Config = _config
}

class TaskJobExample extends TaskJob with Job {

  override type JC = ExampleJobContext
  override type TC = ExampleTaskContext
  override type T = Task[ExampleTaskContext]

  private[this] val _jc: JC = new JC()

  override val jc: JC = _jc

  override def tasks: Seq[ExampleTask] = {
    val tcs: Seq[ExampleTaskContext] = contextConvert(jc)
    // Seq(NewUser(), NewRole())

    Seq.empty
  }

  override def contextConvert: JC => Seq[TC] = jc =>
    jc.loadApps.sorted.map(v => ExampleTaskContext(jc, ConfigFactory.parseMap(Map("app" -> v).asJava)))
}

case class NewUser(tc: ExampleTaskContext) extends ExampleTask {
  override def run(): Unit = info(s"""task $name run... tc.app=${tc.config.getString("app")}""")
}

case class NewRole(tc: ExampleTaskContext) extends ExampleTask {
  override def run(): Unit = info(s"""task $name run... tc.app=${tc.config.getString("app")}""")
}

class Zo extends Input[ExampleTaskContext, Int] {
  override def input(tc: ExampleTaskContext): Int = 0
}

class Add extends Filter[ExampleTaskContext, Int] {
  override def filter(tc: ExampleTaskContext, d: Int): Int = d + 1
}

class Std extends Output[ExampleTaskContext, Int] {
  override def output(tc: ExampleTaskContext, d: Int): Unit = {
    println("Std = d" + d)
  }
} */
