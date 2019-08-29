package com.gourd.norns.core

/**
  * @author Li.Wei by 2019/8/29
  */
trait Context {
  self: Comparable[_] =>
}

class AppContext extends Context with Comparable[AppContext] {
  override def compareTo(o: AppContext): Int = {
    1
  }
}
