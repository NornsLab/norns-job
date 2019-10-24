package io.nornslab.job.core.api

/** 插件
  *
  * @author Li.Wei by 2019/9/2
  */
trait Plugin extends LifecycleAware {

  /** 插件配置模式，声明该插件支持配置参数信息 */
  def configSchema: Seq[ConfigEntry[_]]

}
