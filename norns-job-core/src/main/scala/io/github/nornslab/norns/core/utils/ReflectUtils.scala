package io.github.nornslab.norns.core.utils

import io.github.nornslab.norns.core.api.base.BaseTask
import io.github.nornslab.norns.core.api.{Configuration, JobContext, Task, TaskContext}
import io.github.nornslab.norns.core.plugins.BaseTaskPlugin

/**
  * @author Li.Wei by 2019/9/9
  */
object ReflectUtils {

  def newInstance[T](className: String): T =
    Class.forName(className)
      .getConstructor()
      .newInstance()
      .asInstanceOf[T]

  /** ref [[BaseTask]]
    *
    * @param className className
    * @param tc        data
    */
  def newInstanceBaseTask(className: String,
                          jc: JobContext,
                          tc: TaskContext): Task =
    Class.forName(className)
      .getConstructor(jc.getClass, tc.getClass)
      .newInstance(jc, tc)
      .asInstanceOf[Task]

  /** ref [[BaseTaskPlugin]]
    *
    * @return BaseTaskPlugin
    */
  def newInstanceBaseTaskPlugin[T](className: String,
                                   pluginConfig: Configuration,
                                   jc: JobContext,
                                   tc: TaskContext): T =
    Class.forName(className)
      .getConstructor(classOf[Configuration], jc.getClass, tc.getClass)
      .newInstance(pluginConfig, jc, tc)
      .asInstanceOf[T]

}
