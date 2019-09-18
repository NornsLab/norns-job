package io.github.nornslab.norns.core.api

import java.util.UUID

import com.typesafe.config.Config

/** 任务插件
  *
  * @author Li.Wei by 2019/9/2
  */
trait Plugin extends Service {

  // 插件配置信息
  val pluginConfig: Config

  def getId: String = UUID.randomUUID().toString

}
