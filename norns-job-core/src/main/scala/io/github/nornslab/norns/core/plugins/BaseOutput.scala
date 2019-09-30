package io.github.nornslab.norns.core.plugins

import io.github.nornslab.norns.core.api.{Configuration, Context, LifecycleAware, Output}

/**
  * @author Li.Wei by 2019/9/19
  */
abstract class BaseOutput[E](override val pluginConfig: Configuration,
                             override val context: Context,
                             override val data: Map[String, AnyRef])
  extends BaseTaskPlugin(pluginConfig, context, data) with Output[E] with LifecycleAware
