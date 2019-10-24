package io.nornslab.job.core.api

/** 任务
  * Task 实现支持自定义[[Task]]，支持以插件模式组合为一个Task [[PluginTask]]
  *
  * @author Li.Wei by 2019/9/2
  */
trait Task extends LifecycleAware
