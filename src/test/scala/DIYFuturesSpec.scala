package diy.future

import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class DIYFuturesSpec extends WordSpec with MustMatchers {

  "A DIY Future" must {
    "have an initial empty Promise" in {
      Promise[Int].future.onComplete { case _ => true must be === false }
      true must be === true
    }

    "call onComplete (once) when completed" in {
      val p = Promise[Int]
      val x = new AtomicInteger(0)
      val l = new CountDownLatch(1)
      p.future.onComplete({
        case _ => if (x.incrementAndGet == 1) l.countDown else { true must be === false }
      })
      p.tryComplete(Right(5))
      l.await(5, TimeUnit.SECONDS) must be === true
      x.get must be === 1
    }

    "support multiple onComplete callbacks" in {
      val p = Promise[Int]
      val x = new AtomicInteger(0)
      val l = new CountDownLatch(5)
      for (i <- 1 to 5) p.future.onComplete({
        case _ => if (x.incrementAndGet < 6) l.countDown else { true must be === false }
      })
      p.tryComplete(Right(0))
      l.await(5, TimeUnit.SECONDS) must be === true
      x.get must be === 5
    }

    "support addition of onComplete callbacks AFTER completion" in {
       val p = Promise[Int]
       p.tryComplete(Right(5)) must be === true
       val l = new CountDownLatch(1)
       p.future.onComplete({ case Right(5) => l.countDown })
       l.await(5, TimeUnit.SECONDS) must be === true
    }

    "support be completed with the correct result" in {
       val expected = Left(new RuntimeException("expected"))
       val p = Promise[Int]
       p.tryComplete(expected) must be === true
       p.future.onComplete({
         case result => result must be === expected
       })
    }

  }
}
