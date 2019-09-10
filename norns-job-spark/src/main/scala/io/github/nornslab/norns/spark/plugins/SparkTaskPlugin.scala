package io.github.nornslab.norns.spark.plugins

import com.typesafe.config.Config
import io.github.nornslab.norns.core.{BaseFilter, BaseInput, BaseOutput}
import io.github.nornslab.norns.spark.SJC
import org.apache.spark.sql.{Dataset, Row}

/**
  * @author Li.Wei by 2019/9/5
  */
abstract class SparkInput(private implicit val _pluginInitConfig: Config,
                          private implicit val _tc: (SJC, Config))
  extends BaseInput[SJC, Dataset[Row]]

abstract class SparkFilter(private implicit val _pluginInitConfig: Config,
                           private implicit val _tc: (SJC, Config))
  extends BaseFilter[SJC, Dataset[Row]]

abstract class SparkOutput(private implicit val _pluginInitConfig: Config,
                           private implicit val _tc: (SJC, Config))
  extends BaseOutput[SJC, Dataset[Row]]

