package io.nornslab.job.core.plugins

import io.nornslab.job.core.api.{Input, JobContext, NornsConfig, TaskContext}

/**
  *
  * @param pluginConfig 插件配置信息
  * @param jc           jobContext
  * @param tc           taskContext
  * @tparam JC         jobContext TYPE
  * @tparam PLUG_EVENT PLUG 事件类型
  * @author Li.Wei by 2019/9/19
  */
abstract class BaseInput[JC <: JobContext, PLUG_EVENT](implicit val pluginConfig: NornsConfig,
                                                       implicit val jc: JC,
                                                       implicit val tc: TaskContext)
  extends Input[PLUG_EVENT]
