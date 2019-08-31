package io.github.nornslab.norns.examples.core

import com.typesafe.config.{Config, ConfigFactory}
import io.github.nornslab.norns.core._
import io.github.nornslab.norns.core.utils.Logging
import io.github.nornslab.norns.examples.core.MultiJobEmptySubContextExample.TaskEmpty

/**
  * @author Li.Wei by 2019/8/30
  */
object MultiJobEmptySubContextExample extends Logging {
  type TaskEmpty = Task[EmptyListJobContext]

  def main(args: Array[String]): Unit = NornsMain.work(classOf[MultiJobEmptySubContextExample])
}

class EmptyListJobContext extends JobContext {
  def loadApps: Seq[String] = Seq("1", "2", "3")
}

class MultiJobEmptySubContextExample extends MultiJob {

  override type JC = EmptyListJobContext

  private val _jc: JC = new JC()

  override val jc: JC = _jc

  override def defaultTasks: Seq[TaskEmpty] = Seq(new NewGameUser(), new NewGameRole())

  override def contextConvert: JC => Seq[Config] = _.loadApps.sorted.map(_ => ConfigFactory.empty())
}

private class NewGameUser() extends TaskEmpty {
  override def run(jc: EmptyListJobContext, sjc: Config): Unit = {
    info(s"${this.getClass.getCanonicalName} run...")
  }
}

private class NewGameRole() extends TaskEmpty {
  override def run(jc: EmptyListJobContext, sjc: Config): Unit = {
    info(s"${this.getClass.getCanonicalName} run...")
  }
}
