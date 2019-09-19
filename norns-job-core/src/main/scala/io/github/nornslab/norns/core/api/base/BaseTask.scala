package io.github.nornslab.norns.core.api.base

import io.github.nornslab.norns.core.api.{Context, Task}

/** Task 任务基础类
  *
  * =说明=
  * 请勿在 task 中关闭 context , context 关闭默认由 job 管理
  *
  * @author Li.Wei by 2019/9/2
  */
abstract class BaseTask(val context: Context,
                        val data: Map[String, AnyRef])
  extends Task


