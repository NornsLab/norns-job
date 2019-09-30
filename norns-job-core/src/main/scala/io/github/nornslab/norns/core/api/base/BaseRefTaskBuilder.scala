package io.github.nornslab.norns.core.api.base

import io.github.nornslab.norns.core.api._
import io.github.nornslab.norns.core.utils.Logging
import io.github.nornslab.norns.core.utils.ReflectUtils.{newInstanceBaseTask, newInstanceBaseTaskPlugin}

/**
  * @author Li.Wei by 2019/9/30
  */
class BaseRefTaskBuilder extends TaskBuilder with Logging {

  override type TC = EmptyTaskContext

  /** [[PluginTask]] 中各插件传递数据流类型 */
  type E

  // 反射获取插件时，默认 TaskJob 包地址下 plugins ,如需要重写该参数即可 todo 配置化集中处理
  val pluginPackage: String = s"io.github.nornslab.norns.spark.plugins"
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
  def buildTasks(jc: JobContext)(implicit tc: TC): Seq[Task] =
    if (jc.config.has(multipleTasks)) { // 多任务配置
      jc.config.get[Seq[NornsConfig]](multipleTasks).map(reflectTask(jc, _, tc))
    } else Seq(reflectTask(jc, jc.config, tc))

  /** job jc 转换为多个 task 依赖上下文环境 ，每个 task 依赖上下文环境将被 task执行一次 */
  def buildTaskContexts: Seq[TC] = Seq(EmptyTaskContext.empty)

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
  private[this] def reflectTask(jc: JobContext, c: NornsConfig, tc: TC): Task = {
    info(s"reflectTask by config ----------------------------------------------\n${c.show}")
    if (c.has(taskClassName)) {
      newInstanceBaseTask(c.get[String](taskClassName), jc, tc)
    } else {
      if (!c.has(input) || !c.has(output)) {
        throw new IllegalArgumentException(s"config reflectTask setting [$input,$output]miss")
      } else {
        new BasePluginTask[E]() {
          private val _input = {
            val config = c.get[NornsConfig](CoreConfigKeys.input)
            newInstanceBaseTaskPlugin[Input[E]](inputPackage(config.get[String](plugin)),
              new ConfigurationImpl(config), jc, tc)
          }

          private val _filters = if (c.has(filter)) c.get[Seq[NornsConfig]](filter)
            .map(f => newInstanceBaseTaskPlugin[Filter[E]](filterPackage(f.get[String](plugin)),
              new ConfigurationImpl(f), jc, tc))
          else Seq.empty

          private val _outputs = c.get[Seq[NornsConfig]](output)
            .map(f => newInstanceBaseTaskPlugin[Output[E]](outputPackage(f.get[String](plugin)),
              new ConfigurationImpl(f), jc, tc))

          override def input: Input[E] = _input

          override def filters: Seq[Filter[E]] = _filters

          override def outputs: Seq[Output[E]] = _outputs
        }
      }
    }
  }

}
