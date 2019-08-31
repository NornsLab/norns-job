package io.github.nornslab.norns.core

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory.{empty, systemEnvironment, systemProperties}
import io.github.nornslab.norns.core.Constant.{loadConfFile, renderOptions}
import io.github.nornslab.norns.core.utils.Logging

/** 任务入口
  *
  * =任务运行模式支持=
  * [[Job]] ：单个任务运行
  * [[MultiJob]]  ：多子任务[[Task]]组合为一个任务运行
  *
  * =[[Context]]上下文环境说明=
  * [[Job]]       运行时依赖参数封装为[[JobContext]]，同时默认装载配置信息[[JobContext.config]]
  * [[Task]]      运行时依赖参数封装为[[Config]]
  * 对于[[MultiJob]]模式任务，支持将 [[JobContext]]转换为多个[[Config]]，每个[[Task]]依赖[[Config]]执行一次
  *
  * =任务启动=
  * 统一Main方法入口 [[NornsMain]]
  *
  * 简单示例参考 norns-job-examples 模块中 package com.gourd.norns.examples.core 内容
  *
  * =编码建议=
  * - scala 常用函数式编程，习惯了 def 定义容易，对于资源初始化变量定义为 def（函数），导致多次引用时多次初始化
  * 例如将job中 Job.jc 定义为函数后多次引用时出现多次初始化问题，因添加为私有变量后方法返回，必要时支持 lazy
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
    * 装载后截取[[Constant]]节点数据作为配置项
    */
  private lazy val defaultLoadConfig: Config = {
    val sysConf = empty().withFallback(systemEnvironment).withFallback(systemProperties)

    val r = sysConf
      .withFallback(loadConfFile(None -> nornsJobConf))
      .withFallback(loadConfFile(None -> nornsJobJson))
      .withFallback(loadConfFile(None -> nornsJobProperties))
      .withFallback(loadConfFile(Some(sysConf) -> nornsJobConfig))
      .withOnlyPath(Constant.norns)
    info(r.root().render(renderOptions))
    r
  }
}

case class EmptyJobContext() extends JobContext

///////////////////////////////////////////////////////////////////////////////////////////////////////
// MultiJob 多task构建为单个job
///////////////////////////////////////////////////////////////////////////////////////////////////////
trait MultiJob extends Job {

  /** 默认执行子任务 , 默认为空 */
  def defaultTasks: Seq[Task[JC]] = Seq.empty

  /** 默认执行子任务 + 反射配置文件指定子任务类名 */
  private lazy val _tasks: Seq[Task[JC]] = defaultTasks ++ {
    if (jc.config.hasPathOrNull(jobRunTasks)) {
      val list = jc.config.getStringList(jobRunTasks)
      import scala.collection.JavaConverters._
      list.asScala.map(Class.forName(_).getConstructor().newInstance().asInstanceOf[Task[JC]])
    } else Seq.empty
  }

  def contextConvert: JC => Seq[Config]

  override def run(): Unit = contextConvert(jc).foreach(tc => _tasks.foreach(_.run(jc, tc)))
}

trait Task[JC <: JobContext] extends Logging {

  def run(jc: JC, tc: Config): Unit
}

trait PlugTask {
  type D

  def inputPlug: Input[D]

  def filterPlugs(): Array[Filter[D]] = Array.empty

  def outputPlugs(): Array[Output[D]]

  def run(): Unit = {
    var d = inputPlug.input
    filterPlugs().headOption
    filterPlugs().map(f => {
      d = f.filter(d)
      d
    })
    outputPlugs().foreach(_.output(d))

    // 推导为链式写法 待测试 多输出情况下提供cache操作(可用filter实现，具体根据输出out是否为多个自行定义) 提供并行写出操作
    outputPlugs().foreach {
      _.output(filterPlugs().foldLeft(inputPlug.input)((d, f) => f.filter(d)))
    }
  }

}

class PlugTaskTest extends PlugTask {
  type D = Int

  override def inputPlug: Input[Int] = new Input[Int] {
    override def input: Int = 1
  }

  override def filterPlugs(): Array[Filter[Int]] = Array(
    new Filter[Int] {
      override def filter(d: Int): Int = d - 1
    }, new Filter[Int] {
      override def filter(d: Int): Int = d + 1
    }, new Filter[Int] {
      override def filter(d: Int): Int = d + 1
    }
  )

  override def outputPlugs(): Array[Output[Int]] = Array(
    new Output[Int] {
      override def output(d: Int): Unit = println(s"out $d")
    }
  )
}

object PlugTaskTest {
  def main(args: Array[String]): Unit = {
    new PlugTaskTest().run()
  }
}

trait Plug

trait Input[D] extends Plug {
  def input: D
}

trait Filter[D] extends Plug {
  def filter(d: D): D
}

trait Output[D] extends Plug {
  def output(d: D): Unit
}
