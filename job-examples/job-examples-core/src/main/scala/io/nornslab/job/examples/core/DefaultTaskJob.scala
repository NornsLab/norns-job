package io.nornslab.job.examples.core

import io.nornslab.job.core.api.base.{BaseTaskBuilder, BaseTaskJob}

/**
  * [[BaseTaskJob]] 默认实现
  *
  * @author Li.Wei by 2019/9/3
  */
class DefaultTaskJob extends BaseTaskJob[DefaultJobContext, Serializable] {

  override implicit def context: DefaultJobContext = DefaultJobContext.empty

  val taskBuilderImpl = new BaseTaskBuilder[DefaultJobContext, Serializable]

  override def taskBuilder: BaseTaskBuilder[DefaultJobContext, Serializable] = taskBuilderImpl

}