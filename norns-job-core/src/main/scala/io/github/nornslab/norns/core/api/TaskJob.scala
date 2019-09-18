package io.github.nornslab.norns.core.api

import com.typesafe.config.Config

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
