package io.github.nornslab.norns.examples.flink.raw.streaming


import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

/**
  * Implements a streaming windowed version of the "WordCount" program.
  *
  * This program connects to a server socket and reads strings from the socket.
  * The easiest way to try this out is to open a text sever (at port 12345)
  * using the ''netcat'' tool via
  * {{{
  * nc -l 12345 on Linux
  * nc -l -p 12345 on Windows
  * }}}
  * and run this example with the hostname and the port as arguments..
  *
  * @author Li.Wei by 2019/9/30
  */
object SocketWindowWordCount {

  /** Main program method */
  def main(args: Array[String]): Unit = {

    val params = ParameterTool.fromArgs(args)
    val hostname = if (params.has("hostname")) params.get("hostname") else "localhost"
    val port = if (params.has("port")) params.getInt("port") else 12345

    // get the execution environment
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    // get input data by connecting to the socket
    val text: DataStream[String] = env.socketTextStream(hostname, port, '\n')

    // parse the data, group it, window it, and aggregate the counts
    val windowCounts = text
      .flatMap(w => w.split("\\s"))
      .map(w => WordWithCount(w, 1))
      .keyBy("word")
      .timeWindow(Time.seconds(5))
      .sum("count")

    // print the results with a single thread, rather than in parallel
    windowCounts.print().setParallelism(1)
    env.execute("SocketWindowWordCount")
  }

  /** Data type for words with count */
  case class WordWithCount(word: String, count: Long)

}