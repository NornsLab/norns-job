package io.github.nornslab.norns.core

import com.typesafe.config.Config
import io.github.nornslab.norns.core.utils.ConfigUtils

import scala.collection.JavaConverters._

/**
  *
  * @author Li.Wei by 2019/9/2
  */
trait TaskJob extends Job {
  /**
    * todo 默认反射装载 class
    * t.getConstructor(classOf[(C, Config)]).newInstance(tc).asInstanceOf[Task]
    *
    * @return 运行 Task 类型
    */
  def runningTasks(implicit tc: (C, Config)): Seq[Task] = {
    if (context.config.hasPathOrNull(tasks)) {
      val list = context.config.getConfigList(tasks)
      // import scala.collection.JavaConverters._
      list.asScala.foreach((c: Config) => {
        if (c.hasPathOrNull(className)) {
          Class.forName(c.getString(className)).getConstructor().newInstance().asInstanceOf[Task]
        } else {
          if (c.hasPath(input) && c.hasPath(output)) {
            c.getConfigList(input).asScala.foreach(f => info(s"${f}"))

            /* entrySet().asScala.map(o => {
             val key = o.getKey
             val value = o.getValue
             info(s"key=${key} , value=${value}")
           }) */
          } else throw new IllegalArgumentException("setting error")
        }
      })
    }

    Seq.empty
  }

  /** job context 转换为多个 task 依赖上下文环境 ，每个 task 依赖上下文环境将被 task执行一次 */
  def taskContexts: Seq[(C, Config)] = Seq(context -> ConfigUtils.emptyConfig)

  override def start(): Unit = {
    // todo 反射时区分task、PlugTask
    val runTasks: Seq[Task] = for {
      tc <- taskContexts
      t: Task <- runningTasks(tc)
    } yield t
    runTasks.foreach(_.fastStart().stop())
  }
}
