package io.github.nornslab.norns.examples.core

import com.typesafe.config.{Config, ConfigFactory, ConfigValue}
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

class TaskJobExample extends TaskJob {

  override type JC = ExampleJobContext
  override type TC = ExampleTaskContext

  private[this] val _jc: JC = new JC()

  override val jc: JC = _jc

  override def tasks: Seq[ExampleTask] = Seq(NewUser(), NewRole())

  override def contextConvert: JC => Seq[TC] = jc =>
    jc.loadApps.sorted.map(v => ExampleTaskContext(jc, ConfigFactory.parseMap(Map("app" -> v).asJava)))
}

case class NewUser() extends ExampleTask {
  override def run(tc: ExampleTaskContext): Unit = {
    for (elem: ConfigValue <- tc.jc.config.getList("norns.jobRunTasks").asScala) {
      val origin = elem.atKey("refTask")
      info(origin.root().render(Constant.renderOptions))
      info(s"className=${origin.getString("refTask.className")}")
    }
    info(s"""task $name run... tc.app=${tc.config.getString("app")}""")
  }
}

case class NewRole() extends ExampleTask {
  override def run(tc: ExampleTaskContext): Unit =
    info(s"""task $name run... tc.app=${tc.config.getString("app")}""")
}
