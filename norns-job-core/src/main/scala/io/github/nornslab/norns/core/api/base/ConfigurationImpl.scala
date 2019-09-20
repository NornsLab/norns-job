package io.github.nornslab.norns.core.api.base

import io.github.nornslab.norns.core.api.{Configuration, NornsConfig, PluginConfigSpec}

/**
  * @author Li.Wei by 2019/9/19
  */
class ConfigurationImpl(config: NornsConfig) extends Configuration {

  override def get[T](pcs: PluginConfigSpec[T]): T = {
    val classType: Class[T] = pcs.classType
    val key = pcs.key
    if (config.has(key)) {
      classType.cast(config.underlying.getAnyRef(key))
    } else {
      if (pcs.defaultValue.isDefined) pcs.defaultValue.get
      else throw new IllegalStateException(s"error , Configuration get defaultValue [$key] miss")
      /*
      if (classType == classOf[String]) {
        classType.cast(config.getString(pcs.key))
      } else if (classType == classOf[Int]) {
        classType.cast(config.getInt(pcs.key))
      } else {
        throw new IllegalStateException("error Configuration get")
      } */
    }
  }

}
