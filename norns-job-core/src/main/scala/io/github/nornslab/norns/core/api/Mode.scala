package io.github.nornslab.norns.core.api


/**
  * Application mode, either `Dev`, `Test`, or `Prod`.
  *
  * @author Li.Wei by 2019/9/19
  */
sealed abstract class Mode()

object Mode {

  case object Dev extends Mode()

  case object Test extends Mode()

  case object Prod extends Mode()

  lazy val values: Set[Mode] = Set(Dev, Test, Prod)
}
