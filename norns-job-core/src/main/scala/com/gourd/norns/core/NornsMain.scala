package com.gourd.norns.core

import com.typesafe.config.ConfigFactory.{empty, systemEnvironment, systemProperties}

/** @example java -jar Main -Dnorns.core.job=${job.className}
  * @author Li.Wei by 2019/8/30
  */
object NornsMain {

  def main(args: Array[String]): Unit = work {
    empty().withFallback(systemEnvironment).withFallback(systemProperties).getString(nornsJob)
  }

  implicit def ref(jobClass: String): Job = ref(Class.forName(jobClass))

  implicit def ref(jobClass: Class[_]): Job = jobClass.getConstructor().newInstance().asInstanceOf[Job]

  def work(jobClass: String): Unit = {
    val job = ref(jobClass)
    job.open().run()
    job.close()
  }

  def work(jobClass: Class[_]): Unit = {
    val job = ref(jobClass)
    job.open().run()
    job.close()
  }
}
