package io.github.nornslab.norns.core.plugins

import io.github.nornslab.norns.core.api.{Configuration, Context, Filter, LifecycleAware}

/**
  * @author Li.Wei by 2019/9/19
  */
abstract class BaseFilter[E](override val pluginConfig: Configuration,
                             override val context: Context,
                             override val data: Map[String, AnyRef])
  extends BaseTaskPlugin(pluginConfig, context, data) with Filter[E] with LifecycleAware
