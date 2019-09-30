package io.github.nornslab.norns.examples.flink.raw.batch

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}
import org.apache.flink.streaming.api.scala._

/**
  * @author Li.Wei by 2019/9/30
  */
object WordCount {

  val textFile = ClassLoader.getSystemResource("flink.raw.batch/WordCount.txt").getPath

  def main(args: Array[String]) {

    val env = ExecutionEnvironment.createLocalEnvironment(1)
    val text: DataSet[String] = env.readTextFile(textFile)
    val word = text.flatMap(_.split("\\W+").filter(_.nonEmpty)).map((_, 1)).groupBy(0).sum(1)

    word.print()
    // env.execute("WordCount")
  }
}
