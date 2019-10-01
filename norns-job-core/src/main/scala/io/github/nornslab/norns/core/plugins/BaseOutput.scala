package io.github.nornslab.norns.core.plugins

import io.github.nornslab.norns.core.api._

/**
  * @author Li.Wei by 2019/9/19
  */
abstract class BaseOutput[JC <: JobContext, PLUG_EVENT](implicit val pluginConfig: Configuration,
                                                        implicit val jc: JC,
                                                        implicit val tc: TaskContext)
  extends Output[PLUG_EVENT]
