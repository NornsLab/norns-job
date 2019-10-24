package io.nornslab.job.core.plugins.input

import io.nornslab.job.core.api.{JobContext, NornsConfig, TaskContext}
import io.nornslab.job.core.plugins.BaseInput

/**
  * @author Li.Wei by 2019/9/19
  */
abstract class BaseFile[JC <: JobContext, PLUG_EVENT](implicit override val pluginConfig: NornsConfig,
                                                      implicit override val jc: JC,
                                                      implicit override val tc: TaskContext)
  extends BaseInput[JC, PLUG_EVENT]
