package io.github.nornslab.norns.spark

import com.typesafe.config.{Config, ConfigFactory}
import io.github.nornslab.norns.core.{Task, TaskContext, TaskJob}

/**
  * @author Li.Wei by 2019/9/3
  */
trait SparkTaskJob extends TaskJob with SparkJob {

  override type TC = STC
  override type T = Task[STC]

  override def contextConvert: JC => Seq[TC] = jc => Seq.empty
}

case class SparkTaskContext(private val _jc: SJC,
                            private val _config: Config = ConfigFactory.empty())
  extends TaskContext {
  override def jc: SJC = _jc

  override def config: Config = _config
}


// todo for test
class SparkTaskContext11(private val _jc: SJC,
                         private val _config: Config = ConfigFactory.empty())
  extends TaskContext {
  override def jc: SJC = _jc

  override def config: Config = _config
}