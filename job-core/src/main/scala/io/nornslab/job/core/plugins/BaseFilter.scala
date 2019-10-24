package io.nornslab.job.core.plugins

import io.nornslab.job.core.api.{Filter, JobContext, NornsConfig, TaskContext}

/**
  * @author Li.Wei by 2019/9/19
  */
abstract class BaseFilter[JC <: JobContext, PLUG_EVENT](implicit val pluginConfig: NornsConfig,
                                                        implicit val jc: JC,
                                                        implicit val tc: TaskContext)
  extends Filter[PLUG_EVENT]