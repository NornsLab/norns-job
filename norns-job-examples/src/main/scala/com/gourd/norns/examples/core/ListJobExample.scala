package com.gourd.norns.examples.core

import com.gourd.norns.core._
import com.gourd.norns.core.utils.Logging

/**
  * @author Li.Wei by 2019/8/30
  */
object ListJobExample extends Logging {
  def main(args: Array[String]): Unit = NornsMain.work(classOf[ListJobExample])
}

class ListJobContext extends JobContext {
  def loadApps: Seq[String] = Seq("1", "2", "3")
}

case class AppContext(app: String) extends SubJobContext

class ListJobExample extends ListJob {

  override type JC = ListJobContext

  override type SJC = AppContext

  override def jc: ListJobContext = new ListJobContext()

  override def defaultSubJob: Seq[SubJob[JC, SJC]] = Seq(new NewUser())

  override def contextConvert: ListJobContext => Seq[SJC] = _.loadApps.sorted.map(AppContext)
}

class NewUser extends SubJob[ListJobContext, AppContext] {

  override def run(implicit jc: ListJobContext, sjc: AppContext): Unit = {
    info(s"${this.getClass.getCanonicalName} run")
    info(s"sjc.app=${sjc.app} run...")
  }
}

