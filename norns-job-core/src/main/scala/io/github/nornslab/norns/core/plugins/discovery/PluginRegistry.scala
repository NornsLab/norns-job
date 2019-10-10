package io.github.nornslab.norns.core.plugins.discovery

/**
  * @author Li.Wei by 2019/9/19
  */
class PluginRegistry {

}

object PluginRegistry {
  def main(args: Array[String]): Unit = {
    import scala.reflect.runtime.{universe => ru}
    val classMirror = ru.runtimeMirror(getClass.getClassLoader)
    val symbol = classMirror.staticPackage("")
    symbol.asModule
  }
}

import scala.reflect.runtime.universe._
import scala.reflect.runtime.{currentMirror => cm}

object Test extends App {
  def packageName(sym: Symbol): String = {

    @scala.annotation.tailrec
    def enclosingPackage(sym: Symbol): Symbol = {
      if (sym == NoSymbol) NoSymbol
      else if (sym.isPackage) sym
      else enclosingPackage(sym.owner)
    }

    val pkg = enclosingPackage(sym)

    pkg.alternatives

    if (pkg == cm.EmptyPackageClass) "" else pkg.fullName
  }

  println(packageName(typeOf[Test.type].member(TermName("packageName"))))
  println(packageName(cm.staticPackage("scala")))
  println(packageName(cm.staticClass("scala.collection.immutable.List")))
}
