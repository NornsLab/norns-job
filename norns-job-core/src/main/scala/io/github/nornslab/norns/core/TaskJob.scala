package io.github.nornslab.norns.core

/**
  * 如果未配置 taskClassName 默认按[[PlugTask]]创建匿名类进行启动
  *
  * @author Li.Wei by 2019/9/2
  */
trait TaskJob {
  self: Job =>

  type TC <: TaskContext

  type T <: Task[TC]

  def tasks: Seq[T]

  /* if (jc.config.hasPathOrNull(runTasks)) {
    val list = jc.config.getConfigList(runTasks)
    import scala.collection.JavaConverters._
    list.asScala.map((c: Config) => {
      if (c.hasPathOrNull(className)) {
        Class.forName(c.getString(className)).getConstructor().newInstance().asInstanceOf[T]
      } else {
        if (c.hasPath(input) && c.hasPath(output)) {
          new T {
            override def run(tc: TC): Unit = {

              val inputRef = Class.forName(c.getString(input)).getConstructor().newInstance()
                .asInstanceOf[Input[TC, _]]
              info(s"inputRef=${inputRef}")
            }
          }
        } else throw new IllegalArgumentException("setting error")
      }
    })
  } else Seq[T]() */

  def contextConvert: JC => Seq[TC]

  override def run(): Unit = {
    for {
      tc <- contextConvert(jc)
      t <- tasks
    } yield t.run()

    // contextConvert(jc).foreach(tc => tasks.foreach(_.run(tc)))
  }
}
