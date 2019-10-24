package io.nornslab.job.core.utils

import io.nornslab.job.core.api.{JobContext, NornsConfig, Task, TaskContext}

/**
  * @author Li.Wei by 2019/9/9
  */
object ReflectUtils {

  def newInstance[T](className: String): T =
    Class.forName(className)
      .getConstructor()
      .newInstance()
      .asInstanceOf[T]

  def newInstanceBaseTask(className: String,
                          jc: JobContext,
                          tc: TaskContext): Task =
    Class.forName(className)
      .getConstructor(jc.getClass, tc.getClass)
      .newInstance(jc, tc)
      .asInstanceOf[Task]

  def newInstanceBaseTaskPlugin[T](className: String,
                                   pluginConfig: NornsConfig,
                                   jc: JobContext,
                                   tc: TaskContext): T =
    Class.forName(className)
      .getConstructor(classOf[NornsConfig], jc.getClass, tc.getClass)
      .newInstance(pluginConfig, jc, tc)
      .asInstanceOf[T]

}
