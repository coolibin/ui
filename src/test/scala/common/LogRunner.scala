package common

trait LogRunner {
  lazy protected val log = LogManager.getLogger(this.getClass.getName)
}
