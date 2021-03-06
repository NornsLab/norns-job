package io.nornslab.job.core.api

/** 根据[[JobContext]] 构建 task 列表
  * 实际使用场景
  * 1. 自定义实现task，多个 task 组合为一个job进行运行
  * 2. 插件化配置task，多个 task 组合为一个job进行运行
  * 3. 自定义&插件化配置task，多个 task 组合为一个job进行运行
  *
  * @tparam JC JobContext
  * @author Li.Wei by 2019/10/1
  */
trait TaskBuilder[JC <: JobContext] {

  /**
    * 当前待运行 Task，如需指定具体需要执行实例，重写该方法即可
    * =默认处理逻辑=
    * 如果配置信息中存在 multipleTasks 节点，则默认按多task模式进行实例化
    * 否则按单个 task 模式实例化
    *
    * @return 运行 Task 实例
    */
  def buildTasks(jc: JC)(implicit tc: TaskContext): Seq[Task]

  /** job jc 转换为多个 task 依赖上下文环境 ，每个 task 依赖上下文环境将被 task执行一次 */
  def buildTaskContexts: Seq[TaskContext]

}
