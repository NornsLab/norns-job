package io.github.nornslab.norns.core.api

/**
  * @author Li.Wei by 2019/9/19
  */
trait Configuration {

  def get[T](configSpec: PluginConfigEntry[T]): T

  def get[T](configSpec: PluginConfigEntry[T], jobContext: JobContext, taskContext: TaskContext): T

}
