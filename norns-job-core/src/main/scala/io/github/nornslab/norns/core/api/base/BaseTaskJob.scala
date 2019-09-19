package io.github.nornslab.norns.core.api.base

import com.typesafe.config.Config
import io.github.nornslab.norns.core.api._
import io.github.nornslab.norns.core.utils.ConfigUtils
import io.github.nornslab.norns.core.utils.ReflectUtils._

import scala.collection.JavaConverters._

/**
  * [[TaskJob]] 默认实现
  *
  * @author Li.Wei by 2019/9/2
  */
abstract class BaseTaskJob extends TaskJob {

  /** [[PluginTask]] 中各插件传递数据流类型 */
  type E

  // 反射获取插件时，默认 TaskJob 包地址下 plugins ,如需要重写该参数即可
  val pluginPackage: String = s"${this.getClass.getPackage.getName}.plugins"
  val pluginFilterPackage: String = s"$pluginPackage.filter"
  val pluginOutputPackage: String = s"$pluginPackage.output"
  val pluginInputPackage: String = s"$pluginPackage.input"

  final def inputPackage(name: String): String = s"$pluginInputPackage.$name"

  final def filterPackage(name: String): String = s"$pluginFilterPackage.$name"

  final def outputPackage(name: String): String = s"$pluginOutputPackage.$name"

  /**
    * 当前待运行 Task，如需指定具体需要执行实例，重写该方法即可
    * =默认处理逻辑=
    * 如果配置信息中存在 [[multipleTasks]] 节点，则默认按多task模式进行实例化
    * 否则按单个 task 模式实例化
    *
    * @return 运行 Task 实例
    */
  def runningTasks(data: Map[String, AnyRef]): Seq[Task] =
    if (context.config.hasPathOrNull(multipleTasks)) { // 多任务配置
      context.config.getConfigList(multipleTasks).asScala.map(reflectTask(_, data))
    } else Seq(reflectTask(context.config, data))

  /**
    * 根据配置文件反射创建 task
    * 判断逻辑为
    * 如果配置信息中存在 [[taskClassName]] 节点，则默认实例化指定 task 名称
    * 否则按插件任务模式进行装载一个[[BasePluginTask]]
    *
    * =注意事项=
    * 反射初始化时，尽量使用 val 变量，而不是 def ，保证初始化过程中因配置错误快速失败
    *
    * @param c    反射创建 task 配置信息
    * @param data 创建依赖上下文环境
    * @return 实例化 task
    */
  private[this] def reflectTask(c: Config, data: Map[String, AnyRef]): Task = {
    info(s"reflectTask by config ----------------------------------------------\n${ConfigUtils.render(c)}")
    if (c.hasPathOrNull(taskClassName)) {
      newInstanceBaseTask(c.getString(taskClassName), context, data)
    } else {
      if (!c.hasPath(input) || !c.hasPath(output)) {
        throw new IllegalArgumentException(s"config reflectTask setting [$input,$output]miss")
      } else {
        new BasePluginTask[E]() {
          private val _input = {
            val config = c.getConfig(CoreConfigKeys.input)
            newInstanceBaseTaskPlugin[Input[E]](inputPackage(config.getString(plugin)),
              new ConfigurationImpl(config), context, data)
          }

          private val _filters = if (c.hasPath(filter)) c.getConfigList(filter).asScala
            .map(f => newInstanceBaseTaskPlugin[Filter[E]](filterPackage(f.getString(plugin)),
              new ConfigurationImpl(f), context, data))
          else Seq.empty

          private val _outputs = c.getConfigList(output).asScala
            .map(f => newInstanceBaseTaskPlugin[Output[E]](outputPackage(f.getString(plugin)),
              new ConfigurationImpl(f), context, data))

          override def input: Input[E] = _input

          override def filters: Seq[Filter[E]] = _filters

          override def outputs: Seq[Output[E]] = _outputs
        }
      }
    }
  }

  /** job context 转换为多个 task 依赖上下文环境 ，每个 task 依赖上下文环境将被 task执行一次 */
  def taskData: Seq[Map[String, AnyRef]] = Seq(Map[String, AnyRef]())

  // 待执行 task
  private[this] lazy val tasks: Seq[Task] = {
    val tss = taskData
    info(s"taskData size [${taskData.size}]")
    info(s"build tasks...")
    tss.flatten(runningTasks)
  }

  /** 对所有待执行的 task 执行校验，默认调用每个 task 自身的 init 方法 */
  override def init: Option[Throwable] = tasks.map(_.init).filter(_.isDefined).map(_.get)
    .reduceOption((e1: Throwable, e2: Throwable) => {
      e1.addSuppressed(e2)
      e1
    })

  /**
    * 组合运行逻辑可推导为 for 写法，但是由于 for 中延迟初始化问题，不能在初始化时快速失败退出
    * {{{
    *   val runTasks: Seq[Task] = for {
    *       data <- tss
    *       t <- runningTasks(data)
    *     } yield t
    *     runTasks.foreach(_.fastStart())
    * }}}
    */
  override def start(): Unit = {
    info(s"tasks size [${tasks.size}] , ${tasks.map(_.name)}")

    tasks.foreach(t => {
      info(s"ready for operation task [$t]")
      t.fastStart()
    })
  }
}