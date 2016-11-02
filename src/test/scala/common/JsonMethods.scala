package common

import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization.write
import org.json4s.{DefaultFormats, _}

trait JsonMethods {

  implicit val formats = DefaultFormats

  def jsonCreate(obj: AnyRef): String = {
    write[obj.type](obj) // compact(render()))
  }

  def jsonExtract[T: Manifest](jsonString: String): T = {
    parse(jsonString).extract[T]
  }

  def jsonExtractList[T: Manifest](jsonStr: String): Seq[T] = {
    parse(jsonStr).extract[Seq[T]]
  }
}
