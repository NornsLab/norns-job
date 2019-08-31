package io.github.nornslab.norns.examples.core

import com.typesafe.config.{Config, ConfigFactory}
import io.github.nornslab.norns.core.utils.Logging
import io.github.nornslab.norns.core._
import io.github.nornslab.norns.examples.core.MultiJobExample.TASK
import scala.collection.JavaConverters._

/**
  * @author Li.Wei by 2019/8/30
  */
object MultiJobExample extends Logging {
  type TASK = Task[ListJobContext]

  def main(args: Array[String]): Unit = NornsMain.work(classOf[MultiJobExample])
}

class ListJobContext extends JobContext {
  def loadApps: Seq[String] = Seq("1", "2", "3")
}

class MultiJobExample extends MultiJob {

  override type JC = ListJobContext

  private[this] val _jc: JC = new JC()

  override val jc: JC = _jc

  override def defaultTasks: Seq[TASK] = Seq(NewUser(), NewRole())

  override def contextConvert: ListJobContext => Seq[Config] =
    _.loadApps.sorted.map(v => ConfigFactory.parseMap(Map("app" -> v).asJava))
}

case class NewUser() extends TASK {
  override def run(jc: ListJobContext, tc: Config): Unit = {
    info(s"""${this.getClass.getCanonicalName} run... sjc.app=${tc.getString("app")}""")
  }
}


case class NewRole() extends TASK {
  override def run(jc: ListJobContext, tc: Config): Unit = {
    info(s"""${this.getClass.getCanonicalName} run... sjc.app=${tc.getString("app")}""")
  }
}
