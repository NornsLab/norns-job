package io.github.nornslab.norns.core

import com.typesafe.config.Config
import io.github.nornslab.norns.core.utils.ConfigUtils
import io.github.nornslab.norns.core.utils.ReflectUtils.newInstance

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

/** TaskJob
  *
  * =执行 task 逻辑=
  * 当前待执行 task 为 runningTasks
  * 当前待执行 taskContext 为 taskContexts
  * 即累计运行 task 为 runningTasks * taskContexts ，组合逻辑参考 start 方法
  *
  * @author Li.Wei by 2019/9/2
  */
trait TaskJob extends Job {

  /** [[PluginTask]] 中各插件传递数据流类型 */
  type PDT

  /** 当前待运行 Task，如需指定具体需要执行实例，重写该方法即可 */
  def runningTasks(implicit tc: (C, Config)): Seq[Task]

  /** job context 转换为多个 task 依赖上下文环境 ，每个 task 依赖上下文环境将被 task执行一次 */
  def taskContexts: Seq[(C, Config)]

}

// 默认实现
abstract class BaseTaskJob extends TaskJob {

  // 反射获取插件时，默认 TaskJob 包地址下plugins
  val pluginPackage: String = s"${this.getClass.getPackage.getName}.plugins"
  val pluginFilterPackage: String = s"$pluginPackage.filter"
  val pluginOutputPackage: String = s"$pluginPackage.output"
  val pluginInputPackage: String = s"$pluginPackage.input"

  /**
    * 当前待运行 Task，如需指定具体需要执行实例，重写该方法即可
    * =默认处理逻辑=
    * 如果配置信息中存在 [[multipleTasks]] 节点，则默认按多task模式进行实例化
    * 否则按单个 task 模式实例化
    *
    * @return 运行 Task 实例
    */
  def runningTasks(implicit tc: (C, Config)): Seq[Task] =
    if (context.config.hasPathOrNull(multipleTasks)) { // 多任务配置
      context.config.getConfigList(multipleTasks).asScala.map(reflectTask(_))
    } else Seq(reflectTask(context.config))

  /**
    * 根据配置文件反射创建 task
    * 判断逻辑为
    * 如果配置信息中存在 [[taskClassName]] 节点，则默认实例化指定 task 名称
    * 否则按插件任务模式进行装载一个[[BasePluginTask]]
    *
    * =注意事项=
    * 反射初始化时，尽量使用 val 变量，而不是 def ，保证初始化过程中因配置错误快速失败
    *
    * @param c  反射创建 task 配置信息
    * @param tc 创建依赖上下文环境
    * @return 实例化 task
    */
  def reflectTask(c: Config)(implicit tc: (C, Config)): Task =
    if (c.hasPathOrNull(taskClassName)) newInstance[C, BaseTask[C]](c.getString(taskClassName), tc)
    else {
      if (!c.hasPath(input) || !c.hasPath(output)) {
        throw new IllegalArgumentException("config reflectTask setting miss")
      } else {
        new BasePluginTask[C, PDT]() {
          private val _input = {
            val config = c.getConfig(ConfigKeys.input)
            val className = s"$pluginInputPackage.${config.getString(plugin)}"
            newInstance[C, BaseInput[C, PDT]](className, config, tc)
          }

          private val _filters = if (c.hasPath(filter)) c.getConfigList(filter).asScala
            .map(f => {
              val className = s"$pluginFilterPackage.${f.getString(plugin)}"
              newInstance[C, BaseFilter[C, PDT]](className, f, tc)
            })
          else Seq.empty

          private val _outputs = c.getConfigList(output).asScala
            .map(f => {
              val className = s"$pluginOutputPackage.${f.getString(plugin)}"
              newInstance[C, BaseOutput[C, PDT]](className, f, tc)
            })

          override def input: Input[PDT] = _input

          override def filters: Seq[Filter[PDT]] = _filters

          override def outputs: Seq[Output[PDT]] = _outputs
        }
      }
    }

  /** job context 转换为多个 task 依赖上下文环境 ，每个 task 依赖上下文环境将被 task执行一次 */
  def taskContexts: Seq[(C, Config)] = Seq(context -> ConfigUtils.emptyConfig)


  private[this] lazy val tasks: Seq[Task] = {
    val tss = taskContexts
    info(s"taskContexts size [${taskContexts.size}] , ${taskContexts.map(_._2)}")
    tss.flatten(runningTasks(_))
  }

  /** 启动前初始化操作，参数校验、资源配置信息初始化等操作 */
  override def init: Try[this.type] = Try {
    tasks.foreach(f => {
      f.init match {
        case Failure(exception) => throw exception
        case Success(value) => value
      }
    })
    this
  }

  /**
    * 组合运行逻辑可推导为 for 写法，但是由于 for 中延迟初始化问题，不能在初始化时快速失败退出
    * {{{
    *   val runTasks: Seq[Task] = for {
    *       tc <- tss
    *       t <- runningTasks(tc)
    *     } yield t
    *     runTasks.foreach(_.fastExecute())
    * }}}
    */
  override def start(): Unit = {
    info(s"tasks size [${tasks.size}] , ${tasks.map(_.name)}")

    tasks.foreach(t => {
      info(s"ready for operation task [$t]")
      t.fastExecute()
    })
  }
}