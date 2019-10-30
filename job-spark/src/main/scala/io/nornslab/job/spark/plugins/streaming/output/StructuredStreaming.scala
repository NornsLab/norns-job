package io.nornslab.job.spark.plugins.streaming.output

import io.nornslab.job.core.api.{ConfigEntry, ConfigEntryBuilder, NornsConfig, TaskContext}
import io.nornslab.job.core.plugins.BaseOutput
import io.nornslab.job.spark.SJC
import io.nornslab.job.spark.plugins.streaming.output.StructuredStreaming._
import org.apache.spark.sql.streaming.{DataStreamWriter, Trigger}
import org.apache.spark.sql.{Dataset, Row}

/**
  * 官方文档 http://spark.apache.org/docs/latest/structured-streaming-programming-guide.html#output-sinks
  *
  * @author Li.Wei by 2019/10/23
  */
class StructuredStreaming(implicit override val pluginConfig: NornsConfig,
                          implicit override val jc: SJC,
                          implicit override val tc: TaskContext)
  extends BaseOutput[SJC, Dataset[Row]] {

  private var _dsw: DataStreamWriter[Row] = _

  val format = formatConfig.readFrom(pluginConfig)
  val options = optionsConfig.readFrom(pluginConfig)
  val outputMode = outputModeConfig.readFrom(pluginConfig)
  val partitionBy = partitionByConfig.readFrom(pluginConfig)
  val trigger = triggerConfig.readFrom(pluginConfig)

  override def configSchema: Seq[ConfigEntry[_]] = Seq(formatConfig, optionsConfig, outputModeConfig)

  override def output(event: Dataset[Row]): Unit = {
    _dsw = event.writeStream
      .outputMode(outputMode)
      .options(options)
      .format(format)
      .partitionBy(partitionBy: _*)
      .trigger(trigger)
  }

  override def start(): Unit = {
    super.start()
    _dsw.start()
  }

}

private object StructuredStreaming {

  val formatConfig = ConfigEntryBuilder("format")
    .doc("support see " +
      "http://spark.apache.org/docs/latest/structured-streaming-programming-guide.html#output-sinks")
    .stringConf.create()

  val optionsConfig = ConfigEntryBuilder("options")
    .doc("options support see " +
      "http://spark.apache.org/docs/latest/structured-streaming-programming-guide.html#output-sinks")
    .stringMapConf.create(Some(Map.empty))

  val outputModeConfig = ConfigEntryBuilder("outputMode")
    .doc("support [Append, Update, Complete]")
    .stringConf
    .checkValues(Set("Append", "Update", "Complete"))
    .create()

  val partitionByConfig = ConfigEntryBuilder("partitionBy")
    .doc("partitionBy")
    .seqStringConf.create(Some(Seq.empty))

  val triggerConfig = ConfigEntryBuilder("trigger")
    .doc {
      "support [Continuous, ProcessingTime, Once] . e.g. Continuous#10 seconds, ProcessingTime#10 seconds, Once"
    }
    .conf({ value =>
      val triggerSetting = value.split("#")
      triggerSetting(0) match {
        case "Continuous" => Trigger.Continuous(triggerSetting(1))
        case "ProcessingTime" => Trigger.Continuous(triggerSetting(1))
        case "Once" => Trigger.Once()
      }
    }).create(Some(Trigger.ProcessingTime(0)))
}
