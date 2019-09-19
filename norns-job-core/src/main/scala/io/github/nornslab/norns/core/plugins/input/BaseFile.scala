package io.github.nornslab.norns.core.plugins.input

import io.github.nornslab.norns.core.api.{Configuration, Context}
import io.github.nornslab.norns.core.plugins.BaseInput

/**
  * @author Li.Wei by 2019/9/19
  */
abstract class BaseFile[E](override val pluginConfig: Configuration,
                           override val context: Context,
                           override val data: Map[String, AnyRef])
  extends BaseInput[E](pluginConfig, context, data) {


}
