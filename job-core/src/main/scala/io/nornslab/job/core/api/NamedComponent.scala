package io.nornslab.job.core.api

/**
  * 允许使用名称标记组件，以便引用它在配置系统中唯一
  *
  * @author Li.Wei by 2019/9/30
  */
trait NamedComponent {

  def name: String = getClass.getName

}
