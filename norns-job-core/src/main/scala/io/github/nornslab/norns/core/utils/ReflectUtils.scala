package io.github.nornslab.norns.core.utils

import com.typesafe.config.Config
import io.github.nornslab.norns.core.{BaseTask, BaseTaskPlugin}

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
    * @param tc        tc
    * @tparam C tc 泛型
    * @tparam T 返回数据结构
    * @return T
    */
  def newInstance[C, T](className: String, tc: (C, Config)): T =
    Class.forName(className)
      .getConstructor(tc.getClass)
      .newInstance(tc)
      .asInstanceOf[T]

  /** ref [[BaseTaskPlugin]]
    *
    * @param className    className
    * @param pluginConfig pluginConfig
    * @param tc           tc
    * @tparam C tc 泛型
    * @tparam T 返回数据结构
    * @return T
    */
  def newInstance[C, T](className: String, pluginConfig: Config, tc: (C, Config)): T =
    Class.forName(className)
      .getConstructor(classOf[Config], tc.getClass)
      .newInstance(pluginConfig, tc)
      .asInstanceOf[T]

}
