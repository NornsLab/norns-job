package com.gourd.norns.core

import com.gourd.norns.core.Constant.{loadConfFile, norns}
import com.gourd.norns.core.utils.Logging
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory.{empty, systemEnvironment, systemProperties}

/** 任务入口
  *
  * =任务运行模式支持=
  * [[Job]] ：单个任务运行
  * [[MultiJob]]  ：多子任务[[Task]]组合为一个任务运行
  *
  * =[[Context]]上下文环境说明=
  * [[Job]]       运行时依赖参数封装为[[JobContext]]，同时默认装载配置信息[[JobContext.config]]
  * [[Task]]      运行时依赖参数封装为[[TaskContext]]
  * 对于[[MultiJob]]模式任务，支持将 [[JobContext]]转换为多个[[TaskContext]]，每个[[Task]]依赖[[TaskContext]]执行一次
  *
  * =任务启动=
  * 统一Main方法入口 [[NornsMain]]
  *
  * 简单示例参考 norns-job-examples 模块中 package com.gourd.norns.examples.core 内容
  *
  * @author Li.Wei by 2019/8/29
  */
trait Job extends Logging with AutoCloseable {

  type JC <: JobContext

  /** 任务名称 */
  def name: String = getClass.getCanonicalName

  /** [[run()]] 执行前 打开资源操作 */
  def initialize(): this.type = this

  /** job 上下文参数 */
  def jc: JC

  /** job 运行 */
  def run(): Unit

  /** job 运行结束资源关闭 */
  override def close(): Unit = {
    try {
      jc.close()
    } catch {
      case e: Exception => error("jc.close error", e)
    }
  }
}

trait JobContext extends Context {

  def config: Config = JobContext.defaultLoadConfig
}

object JobContext extends Logging {
  /**
    * 为简化配置操作，不引用 main 函数传入 args参数，推荐使用系统参数（-D）或者配置文件
    * =配置装载顺序=
    * 系统环境变量 env
    * 系统参数 -D
    * 默认配置文件（非必须）     norns-job.conf , norns-job.json , norns-job.properties
    * 自定义配置文件（推荐使用    -Dnorns.job.config=${path}指定）
    *
    * 装载后截取[[Constant.norns]]节点数据作为配置项
    */
  private lazy val defaultLoadConfig: Config = {
    val sysConf = empty().withFallback(systemEnvironment).withFallback(systemProperties)

    val r = sysConf
      .withFallback(loadConfFile(None -> nornsJobConf))
      .withFallback(loadConfFile(None -> nornsJobJson))
      .withFallback(loadConfFile(None -> nornsJobProperties))
      .withFallback(loadConfFile(Some(sysConf) -> nornsJobConfig))
      .withOnlyPath(norns)
    info(r.root().render(Constant.renderOptions))
    r
  }
}

case class EmptyJobContext() extends JobContext

///////////////////////////////////////////////////////////////////////////////////////////////////////
// MultiJob 多task构建为单个job
///////////////////////////////////////////////////////////////////////////////////////////////////////
trait MultiJob extends Job {

  type TC <: TaskContext

  /** 默认执行子任务 , 默认为空 */
  def defaultTasks: Seq[Task[JC, TC]] = Seq.empty

  /** 默认执行子任务 + 反射配置文件指定子任务类名 */
  protected def tasks: Seq[Task[JC, TC]] = defaultTasks ++ {
    if (jc.config.hasPathOrNull(jobRunTasks)) {
      val list = jc.config.getStringList(jobRunTasks)
      import scala.collection.JavaConverters._
      list.asScala.map(Class.forName(_).getConstructor().newInstance().asInstanceOf[Task[JC, TC]])
    } else Seq[Task[JC, TC]]()
  }

  def contextConvert: JC => Seq[TC]

  override def run(): Unit = contextConvert(jc).foreach((tc: TC) => tasks.foreach(_.run(jc, tc)))
}

trait Task[JC <: JobContext, TC <: TaskContext] extends Logging {

  def run(jc: JC, tc: TC): Unit
}

trait TaskContext extends Context

case class EmptyTaskContext() extends TaskContext
