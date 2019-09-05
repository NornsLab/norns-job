package io.github.nornslab.norns.core

import com.typesafe.config.Config
import io.github.nornslab.norns.core.utils.ConfigUtils

/**
  *
  * @author Li.Wei by 2019/9/2
  */
trait TaskJob extends Job {
  // self: Job =>
  /**
    * todo 默认反射装载 class
    *
    * @return 运行 Task 类型
    */
  def tasks: Seq[Class[_]] = Seq.empty

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

  /** job context 转换为多个 task 依赖上下文环境 ，每个 task 依赖上下文环境将被 task执行一次 */
  def taskContexts: Seq[(C, Config)] = Seq(context -> ConfigUtils.emptyConfig)

  override def start(): Unit = {
    // todo 反射时区分task、PlugTask
    val runTasks: Seq[Task] = for {
      tc <- taskContexts
      t <- tasks
    } yield t.getConstructor(classOf[(C, Config)]).newInstance(tc).asInstanceOf[Task]
    runTasks.foreach(_.fastStart().stop())
  }
}
