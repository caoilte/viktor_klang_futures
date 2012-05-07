import java.util.concurrent.Future

object FirstFuture {
  
  // cpu intensive
  // race (won't find the first one necessarily)
  // uses a var
  def naiveFirstCompletedOf[T](t: Traversable[Future[T]]): Future[T] = {
    var first: Option[Future[T]] = None
    do {
      first = t.find(_.isDone)
    } while (first.isEmpty)
    first.get
  }
  
//  class MyFuture(val f1:Future[Int], val f2:Future[Int]) extends Future[Int] {
//
//
//  }

//  def plus (f1: Future[Int], f2: Future[Int]): Future[Int] = {
//    return new MyFuture(f1, f2);
//
//  }
}