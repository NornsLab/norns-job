package io.github.nornslab.norns.core.api

import java.util.UUID

import io.github.nornslab.norns.core.utils.Logging

/** 任务插件
  *
  * @author Li.Wei by 2019/9/2
  */
trait Plugin extends Logging {

  def configSchema: Seq[PluginConfigSpec[_]]

  def id: String = UUID.randomUUID().toString

}
