package example.spec

import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers

class ExampleSpec extends WordSpec with MustMatchers {

  "An Example Spec" must {
    "show how to write a spec" in {
      1 must be(1)
    }
  }
}
