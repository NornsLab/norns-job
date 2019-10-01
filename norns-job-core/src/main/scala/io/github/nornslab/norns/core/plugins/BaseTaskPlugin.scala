package io.github.nornslab.norns.core.plugins

import io.github.nornslab.norns.core.api._

/**
  * [[Plugin]] 基础实现
  *
  * @param pluginConfig 插件初始化配置信息
  * @param jc           插件依赖JobContext参数
  * @param tc           插件依赖数据参数
  * @author Li.Wei by 2019/9/2
  */
class BaseTaskPlugin[JC <: JobContext](implicit val pluginConfig: Configuration,
                                       implicit val jc: JC,
                                       implicit val tc: TaskContext)
  extends Plugin {
  // 插件配置信息 = 初始化插件配置信息+默认配置信息（非必填默认进行配置填充）todo 覆盖问题待测试
  /* val pluginConfig: Config = {
     val config = ConfigFactory.parseMap {
       supportConfig.filter(_.default.isDefined).map(c => c.key -> c.default.get).toMap.asJava
     }.withFallback(getConfiguration)
     info(
       s"""$id
          |pluginConfig=
          |${ConfigUtils.render(config)}
          |dataConfig=
          |${ConfigUtils.render(dataConfig)}""".stripMargin)
     config
   }
 */
  /** 取所有支持配置项中必填项进行配置项存在与否校验 */
  /*
    override def init: Option[Throwable] = {
      val exceptions = supportConfig
        .filter(_.default.isEmpty)
        .map(k => Try(k.check(pluginConfig)) match {
          case Failure(exception) => Some(exception)
          case _ => None
        })
        .filter(_.isDefined).map(_.get)

      if (exceptions.isEmpty) None
      else Some(
        exceptions.fold[Throwable](new Exception(
          s"""init $id error,
             | pluginConfig =${ConfigUtils.render(pluginConfig)}
             | supportConfig=$supportConfig
              """.stripMargin))((e1: Throwable, e2: Throwable) => {
          e1.addSuppressed(e2)
          e1
        })
      )
    }
  */

  /** 插件支持配置项 */
  // def supportConfig: Seq[ConfigKey] = Seq.empty

  override def configSchema: Seq[PluginConfigSpec[_]] = Seq.empty
}








