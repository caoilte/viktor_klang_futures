package diy.future

trait Future[+T] {
  type Result

  def onComplete[U](pf: PartialFunction[Result, U]): Unit
  //protected def newPromise[T]: Promise[T]
  //def map[U](f: T => U): Future[U]
  //def flatMap[U](f: T => Future[U]): Future[U]
  //def foreach[U](f: T => U): Unit
  //def recover[A >: T](pf: PartialFunction[Throwable, A]): Future[A]
  //def zip[U](that: Future[U]): Future[(T, U)]
  //def isCompleted: Boolean
  //def onSuccess[U](pf: PartialFunction[T, U]): Unit
  //def onFailure[U](pf: PartialFunction[Throwable, U]): Unit
  //def failed: Future[Throwable]
  //def fallbackTo[U >: T](that: Future[U]): Future[U]
  //def recoverWith[U >: T](pf: PartialFunction[Throwable, Future[U]]): Future[U]
  //def andThen[U](pf: PartialFunction[Either[Throwable, T], U]): Future[T]
}

trait Promise[T] {
  type Result
  def tryComplete(result: Result): Boolean
  def future: Future[T]
  //def completeWith(future: Future[T])
  //def completeWith(other: Future[T]): this.type
}

object Promise {
  def apply[T] = new PromiseImpl[T]
}

import java.util.concurrent.atomic.AtomicReference
import scala.annotation.tailrec
private[future] final class PromiseImpl[T] extends AtomicReference[AnyRef](Nil) with Promise[T] with Future[T] {
  type Result = Either[Throwable, T]
  type Callback = PartialFunction[Result, Any]
  type Callbacks = List[Callback]

  @tailrec override final def onComplete[U](pf: PartialFunction[Result, U]): Unit = get match {
    case callbacks: Callbacks => if (!compareAndSet(callbacks, pf :: callbacks)) onComplete(pf)
    case result: Result => if (pf isDefinedAt result) pf(result)
  }

  override def tryComplete(result: Result): Boolean = get match {
    case callbacks: Callbacks =>
      if (!compareAndSet(callbacks, result)) tryComplete(result)
      else {
        callbacks foreach { c => if (c isDefinedAt result) c(result) }
        true
      }
    case _ => false
  }
  override def future: Future[T] = this
  override def toString = ""
}