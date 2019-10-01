package io.github.nornslab.norns.core.plugins

import io.github.nornslab.norns.core.api._

/**
  * @author Li.Wei by 2019/9/19
  */
abstract class BaseOutput[JC <: JobContext, PLUG_EVENT](implicit override val pluginConfig: Configuration,
                                                        implicit override val jc: JC,
                                                        implicit override val tc: TaskContext)
  extends BaseTaskPlugin[JC] with Output[PLUG_EVENT] with LifecycleAware
