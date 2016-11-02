package common

import org.json4s.DefaultFormats
import org.json4s.native.Serialization._

trait JsonDataExt {
  implicit val formats = DefaultFormats

  def asJson: String = {
    write(this)
  }

  def asPrettyJson: String = {
    writePretty(this)
  }
}
