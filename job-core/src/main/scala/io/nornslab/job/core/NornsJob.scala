package io.nornslab.job.core

import com.typesafe.config.ConfigFactory.{empty, systemEnvironment, systemProperties}
import io.nornslab.job.core.api.{Job, JobContext}

/** @example java -jar Main -Dnorns.core.job=${job.className}
  * @author Li.Wei by 2019/8/30
  */
object NornsJob {
  val nornsJob = s"norns.core.job"

  def main(args: Array[String]): Unit = work {
    empty().withFallback(systemEnvironment).withFallback(systemProperties).getString(nornsJob)
  }

  implicit def ref(jobClass: String): Job[JobContext] = ref(Class.forName(jobClass))

  implicit def ref(jobClass: Class[_]): Job[JobContext] =
    jobClass.getConstructor().newInstance().asInstanceOf[Job[JobContext]]

  def work(jobClass: String): Unit = ref(jobClass).fastExecute()

  def work(jobClass: Class[_]): Unit = ref(jobClass).fastExecute()
}
