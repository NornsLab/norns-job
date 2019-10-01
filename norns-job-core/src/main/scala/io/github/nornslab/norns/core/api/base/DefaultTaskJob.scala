package io.github.nornslab.norns.core.api.base

/**
  * @author Li.Wei by 2019/9/3
  */
class DefaultTaskJob extends BaseTaskJob[DefaultJobContext, Serializable] {

  override implicit def context: DefaultJobContext = DefaultJobContext.empty

}