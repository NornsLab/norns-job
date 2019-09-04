package io.github.nornslab.norns.spark

import com.typesafe.config.Config
import io.github.nornslab.norns.core._
import org.apache.spark.sql.{Dataset, Row}

/**
  * @author Li.Wei by 2019/9/3
  */
case class SparkTask(private implicit val _tc: (SJC, Config))
  extends BaseTask[SJC] {

  override def start(): Unit = {} // 默认实现
}


class SparkPlugTask(private implicit val _tc: (SJC, Config),
                    private implicit val _inputPlug: SpInput,
                    private implicit val _filterPlugs: Array[SpFilter],
                    private implicit val _outputPlugs: Array[SpOutput])
  extends BasePlugTask[SJC, Dataset[Row]]




