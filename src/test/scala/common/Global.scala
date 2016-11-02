package common

object Global {

  def dropTail(origString: String, tail: String): String = {
    if (origString.takeRight(tail.length) == tail) {
      origString.dropRight(tail.length)
    } else {
      origString
    }
  }
}
