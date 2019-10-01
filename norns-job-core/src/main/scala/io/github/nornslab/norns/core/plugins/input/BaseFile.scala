package io.github.nornslab.norns.core.plugins.input

import io.github.nornslab.norns.core.api.{Configuration, JobContext, TaskContext}
import io.github.nornslab.norns.core.plugins.BaseInput

/**
  * @author Li.Wei by 2019/9/19
  */
abstract class BaseFile[JC <: JobContext, PLUG_EVENT](implicit override val pluginConfig: Configuration,
                                                      implicit override val jc: JC,
                                                      implicit override val tc: TaskContext)
  extends BaseInput[JC, PLUG_EVENT]
