package io.github.nornslab.norns.core.api

/** 任务
  *
  * C = Task 依赖当前 job 上下文环境
  *
  * =说明=
  * 请勿在 task 中关闭 context , context 关闭默认由 job 管理
  *
  * @author Li.Wei by 2019/9/2
  */
trait Task extends Service {
  override type C <: JobContext
}
