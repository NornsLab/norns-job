package io.nornslab.job.core.api

/** Processors 插件
  * https://www.elastic.co/guide/en/elasticsearch/reference/7.4/ingest-processors.html
  *
  * @tparam E 插件处理流程依赖数据结构
  */
trait Processors[E] extends Plugin {

  def process(event: E): E

}
