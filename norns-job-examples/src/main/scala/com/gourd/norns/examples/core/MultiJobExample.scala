package com.gourd.norns.examples.core

import com.gourd.norns.core._
import com.gourd.norns.core.utils.Logging
import com.gourd.norns.examples.core.MultiJobExample.TASK

/**
  * @author Li.Wei by 2019/8/30
  */
object MultiJobExample extends Logging {
  type TASK = Task[ListJobContext, AppContext]

  def main(args: Array[String]): Unit = NornsMain.work(classOf[MultiJobExample])
}

class ListJobContext extends JobContext {
  def loadApps: Seq[String] = Seq("1", "2", "3")
}

case class AppContext(app: String) extends TaskContext

class MultiJobExample extends MultiJob {

  override type JC = ListJobContext

  override type TC = AppContext

  override def jc: ListJobContext = new ListJobContext()

  override def defaultTasks: Seq[TASK] = Seq(NewUser(), NewRole())

  override def contextConvert: ListJobContext => Seq[TC] = _.loadApps.sorted.map(AppContext)
}

case class NewUser() extends TASK {
  override def run(jc: ListJobContext, tc: AppContext): Unit = {
    info(s"${this.getClass.getCanonicalName} run... sjc.app=${tc.app}")
  }
}


case class NewRole() extends TASK {
  override def run(jc: ListJobContext, tc: AppContext): Unit = {
    info(s"${this.getClass.getCanonicalName} run... sjc.app=${tc.app}")
  }
}
