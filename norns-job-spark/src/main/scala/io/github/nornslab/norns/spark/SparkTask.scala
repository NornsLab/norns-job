package io.github.nornslab.norns.spark

import io.github.nornslab.norns.core._
import org.apache.spark.sql.{Dataset, Row}

/**
  * @author Li.Wei by 2019/9/3
  */
case class SparkTask(implicit val tc: STC) extends Task[STC] {

  implicit final val jc: SJC = tc.jc

  override def run(): Unit = {}
}

abstract class SparkPlugTask(implicit override val tc: STC)
  extends SparkTask
    with PlugTask[STC, Dataset[Row]] {

  override def inputPlug: SpInput

  override def filterPlugs(): Array[SpFilter] = Array.empty

  override def outputPlugs(): Array[SpOutput]
}
