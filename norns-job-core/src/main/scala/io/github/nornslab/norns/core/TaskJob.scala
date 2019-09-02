package io.github.nornslab.norns.core

/**
  * @author Li.Wei by 2019/9/2
  */
trait TaskJob extends Job {

  type TC <: TaskContext

  type T = Task[TC]

  def tasks: Seq[T] = {
    if (jc.config.hasPathOrNull(jobRunTasks)) {
      val list = jc.config.getStringList(jobRunTasks)
      import scala.collection.JavaConverters._
      list.asScala.map(Class.forName(_).getConstructor().newInstance().asInstanceOf[T])
    } else Seq.empty
  }

  def contextConvert: JC => Seq[TC]

  override def run(): Unit = {
    for {
      tc <- contextConvert(jc)
      t <- tasks
    } yield t.run(tc)

    // contextConvert(jc).foreach(tc => tasks.foreach(_.run(tc)))
  }
}
