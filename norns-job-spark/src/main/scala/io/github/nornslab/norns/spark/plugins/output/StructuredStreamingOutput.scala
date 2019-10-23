package io.github.nornslab.norns.spark.plugins.output

import io.github.nornslab.norns.core.api.{ConfigEntry, Configuration, TaskContext}
import io.github.nornslab.norns.core.plugins.BaseOutput
import io.github.nornslab.norns.spark.SJC
import io.github.nornslab.norns.spark.plugins.PluginConfigEntry.{formatConfig, optionsConfig, outputModeConfig}
import org.apache.spark.sql.streaming.DataStreamWriter
import org.apache.spark.sql.{Dataset, Row}

/**
  * 官方文档 http://spark.apache.org/docs/2.4.4/structured-streaming-programming-guide.html#output-sinks
  *
  * @author Li.Wei by 2019/10/23
  */
class StructuredStreamingOutput(implicit override val pluginConfig: Configuration,
                                implicit override val jc: SJC,
                                implicit override val tc: TaskContext)
  extends BaseOutput[SJC, Dataset[Row]] {

  private var _dsw: DataStreamWriter[Row] = _

  val format = pluginConfig.get(formatConfig)
  val options = pluginConfig.get(optionsConfig)
  val outputMode = pluginConfig.get(outputModeConfig)

  override def configSchema: Seq[ConfigEntry[_]] = Seq(formatConfig, optionsConfig, outputModeConfig)

  override def output(event: Dataset[Row]): Unit = {
    _dsw = event.writeStream
      .outputMode(outputMode)
      .options(options)
      .format(format)
      // .trigger(Trigger.Continuous("1 second"))  // todo 该参数不可使用字符串创建，需设计传参格式
  }

  override def start(): Unit = {
    super.start()
    _dsw.start()
    jc.sparkSession.streams.awaitAnyTermination() // todo awaitAnyTermination 方法应该交于 job 级别处理
  }

}
