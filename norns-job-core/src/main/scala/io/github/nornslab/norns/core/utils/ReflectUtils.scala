package io.github.nornslab.norns.core.utils

import io.github.nornslab.norns.core.api.base.BaseTask
import io.github.nornslab.norns.core.api.{Configuration, Context}
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
    * @param data      data
    */
  def newInstanceBaseTask(className: String,
                          context: Context,
                          data: Map[String, AnyRef]): BaseTask =
    Class.forName(className)
      .getConstructor(context.getClass, classOf[Map[String, AnyRef]])
      .newInstance(context, data)
      .asInstanceOf[BaseTask]

  /** ref [[BaseTaskPlugin]]
    *
    * @return BaseTaskPlugin
    */
  def newInstanceBaseTaskPlugin[T](className: String,
                                   pluginConfig: Configuration,
                                   context: Context,
                                   data: Map[String, AnyRef]): T =
    Class.forName(className)
      .getConstructor(classOf[Configuration], context.getClass, classOf[Map[String, AnyRef]])
      .newInstance(pluginConfig, context, data)
      .asInstanceOf[T]

}
