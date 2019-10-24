package io.nornslab.job.core.api.base

import io.nornslab.job.core.api._
import io.nornslab.job.core.utils.Logging
import io.nornslab.job.core.utils.ReflectUtils.{newInstanceBaseTask, newInstanceBaseTaskPlugin}

/**
  * @author Li.Wei by 2019/10/1
  */
class BaseTaskBuilder[JC <: JobContext, PLUG_EVENT <: Serializable] extends TaskBuilder[JC] with Logging {

  /**
    * 当前待运行 Task，如需指定具体需要执行实例，重写该方法即可
    * =默认处理逻辑=
    * 如果配置信息中存在 [[multipleTasks]] 节点，则默认按多task模式进行实例化
    * 否则按单个 task 模式实例化
    *
    * @return 运行 Task 实例
    */
  def buildTasks(jc: JC)(implicit tc: TaskContext): Seq[Task] =
    if (jc.config.has(multipleTasks)) { // 多任务配置
      jc.config.get[Seq[NornsConfig]](multipleTasks).map(reflectTask(jc, _, tc))
    } else Seq(reflectTask(jc, jc.config, tc))

  /** job jc 转换为多个 task 依赖上下文环境 ，每个 task 依赖上下文环境将被 task执行一次 */
  def buildTaskContexts: Seq[TaskContext] = Seq(TaskContext.empty)

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
  private[this] def reflectTask(jc: JobContext, c: NornsConfig, tc: TaskContext): Task = {
    info(s"reflectTask by config ----------------------------------------------\n${c.show}")
    if (c.has(taskClassName)) {
      newInstanceBaseTask(c.get[String](taskClassName), jc, tc)
    } else {
      if (!c.has(input) || !c.has(output)) {
        throw new IllegalArgumentException(s"config reflectTask setting [$input,$output]miss")
      } else {
        new BasePluginTask[PLUG_EVENT]() {
          private val _input = {
            val config = c.get[NornsConfig](CoreConfigKeys.input)
            newInstanceBaseTaskPlugin[Input[PLUG_EVENT]](config.get[String](plugin), config, jc, tc)
          }

          private val _filters = if (c.has(filter)) c.get[Seq[NornsConfig]](filter).map(f =>
            newInstanceBaseTaskPlugin[Filter[PLUG_EVENT]](f.get[String](plugin), f, jc, tc))
          else Seq.empty

          private val _outputs = c.get[Seq[NornsConfig]](output).map(f =>
            newInstanceBaseTaskPlugin[Output[PLUG_EVENT]](f.get[String](plugin), f, jc, tc))

          override def input: Input[PLUG_EVENT] = _input

          override def filters: Seq[Filter[PLUG_EVENT]] = _filters

          override def outputs: Seq[Output[PLUG_EVENT]] = _outputs
        }
      }
    }
  }

}
