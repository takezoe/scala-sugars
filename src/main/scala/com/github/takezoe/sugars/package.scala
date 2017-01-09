package com.github.takezoe

import scala.language.reflectiveCalls

package object sugars {

  def ignoreException[T](f: => T): Option[T] = {
    try {
      Some(f)
    } catch {
      case e: Exception => None
    }
  }

  def defining[T, R](value: T)(f: T => R): R = {
    f(value)
  }

  // Loan pattern
  def using[A <% { def close(): Unit }, B](resource: A)(f: A => B): B =
    try f(resource) finally {
      if(resource != null){
        ignoreException { resource.close() }
      }
    }

  def using[A <% { def close(): Unit }, B <% { def close(): Unit }, C](resource1: A, resource2: B)(f: (A, B) => C): C =
    try f(resource1, resource2) finally {
      if(resource1 != null){
        ignoreException { resource1.close() }
      }
      if(resource2 != null){
        ignoreException { resource2.close() }
      }
    }

  implicit class AnyOps[T](value: T){
    // unsafeTap
    def <|(f: T => Unit): T = {
      f(value)
      value
    }

    // type-safe comparison
    def === (target: T): Boolean = value == target
  }

  // Union type
  sealed trait |[A, B] {
    def value: Any
    def as[T]: T = value.asInstanceOf[T]
  }

  private case class Union[A, B](_1: Option[A], _2: Option[B]) extends |[A, B]{
    def value: Any = (_1 match {
      case Some(x: |[_, _]) => Some(x.value)
      case x => x
    }).getOrElse(_2.get)
  }

  implicit def union2FromLeftType[A, B](a: A): A | B = Union(Some(a), None)
  implicit def union2FromRightType[A, B](a: B): A | B = Union(None, Some(a))
  implicit def union3FromLeftType[A, B, C](a: A): A | B | C = Union(Some(a), None)
  implicit def union3FromRightType[A, B, C](a: B): A | B | C = Union(Some(a), None)
  implicit def union4FromLeftType[A, B, C, D](a: A): A | B | C | D = Union(Some(a), None)
  implicit def union4FromRightType[A, B, C, D](a: B): A | B | C | D = Union(Some(a), None)

}
