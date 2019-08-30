package com.gourd.norns.core

import com.gourd.norns.core.Constant.{loadConfFile, norns}
import com.gourd.norns.core.utils.Logging
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory.{empty, systemEnvironment, systemProperties}

/**
  * @author Li.Wei by 2019/8/29
  */
trait ListJob extends Job {

  type SJC <: SubJobContext

  /** 默认执行子任务 , 默认为空 */
  def defaultSubJob: Seq[SubJob[JC, SJC]] = Seq.empty

  /** 默认执行子任务 + 反射配置文件指定子任务类名 */
  protected def jobs: Seq[SubJob[JC, SJC]] = defaultSubJob ++ {
    if (jc.config.hasPathOrNull(jobRunSubJobs)) {
      val list = jc.config.getStringList(jobRunSubJobs)
      import scala.collection.JavaConverters._
      list.asScala.map(Class.forName(_).getConstructor().newInstance().asInstanceOf[SubJob[JC, SJC]])
    } else Seq[SubJob[JC, SJC]]()
  }

  def contextConvert: JC => Seq[SJC]

  override def run(): Unit = contextConvert(jc).foreach((s: SJC) => jobs.foreach(_.run(jc, s)))
}

trait SubJob[JC <: JobContext, SJC <: SubJobContext] extends Logging {

  def run(implicit jc: JC, sjc: SJC): Unit
}

trait SubJobContext extends Context {

  def config: Config = SubJobContext.defaultLoadConfig
}

object SubJobContext extends Logging {
  /**
    * =配置装载顺序=
    * 系统环境变量 env
    * 系统参数 -D
    * 自定义配置文件（推荐使用    -Dnorns.core.listJobConfig=${path}指定）
    *
    * 装载后截取[[Constant.norns]]节点数据作为配置项
    */
  private lazy val defaultLoadConfig: Config = {
    val sysConf = empty().withFallback(systemEnvironment).withFallback(systemProperties)

    val r = sysConf
      .withFallback(loadConfFile(Some(sysConf) -> listJobConfig))
      .withOnlyPath(norns)
    info(r.root().render(Constant.renderOptions))
    r
  }
}



