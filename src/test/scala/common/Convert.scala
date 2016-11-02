package common

import java.text.SimpleDateFormat
import java.util.Date

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

object Convert {

  /**
    * Convert string as it returned from a database to simple date format
    *
    * @param dateString
    * @return
    */
  def dbStrToSimpleDate(dateString: String, pattern: String = "yyyy-MM-dd"): Date = {
    new SimpleDateFormat(pattern).parse(dateString.split("""\ """)(0))
  }

  def toJodaDate(s: String, pattern: String = "yyyy-MM-dd HH:mm:ss.s"): DateTime = {
    DateTime.parse(s.take(pattern.length), DateTimeFormat.forPattern(pattern))
  }

  def toJodaDateOpt(s: Option[String], pattern: String = "yyyy-MM-dd"): Option[DateTime] = {
    s match {
      case None => None
      case _ => Some(DateTime.parse(s.get.take(pattern.length), DateTimeFormat.forPattern(pattern)))
    }
  }

  def yesNoUndefined2BooleanOption(flag: String): Option[Boolean] = {
    flag.toLowerCase match {
      case "y" | "yes" | "true" => Some(true)
      case "n" | "no" | "false" => Some(false)
      case _ => None
    }
  }

  def yesNo2BooleanOpt(flag: String): Option[Boolean] = {
    flag.toLowerCase match {
      case "y" | "yes" | "true" => Some(true)
      case "n" | "no" | "false" => Some(false)
      case _ => None
    }
  }

  /**
    * Takes [Any] and returns Int
    *
    * @param: Any
    * @return
    */
  def toInt: (Any) => Int = {
    case i: Int => i
    case f: Float => f.toInt
    case d: Double => d.toInt
    case s: String => if (s.isEmpty) 0 else s.toInt
  }

  def str2Double(strVal: String) = {
    if (!strVal.isEmpty) strVal.toDouble else 0.0
  }

  /**
    * Converts string value to an Option of the requested type
    *
    * @param anyString
    * @tparam T
    * @return
    */
  def stringToOption[T](anyString: String): Option[T] = {
    anyString match {
      case "" | "None" => None
      case _ =>
        Some(anyString.asInstanceOf[T])
    }
  }

  def stringToIntOption(anyString: String): Option[Int] = {
    if (anyString.isEmpty) None else Some(toInt(anyString))
  }

  def caseClassToMap(c: Product): Map[String, Option[String]] = {
    (Map[String, Option[String]]() /: c.getClass.getDeclaredFields) { (a, f) =>
      f.setAccessible(true)
      a + (f.getName -> f.get(c).asInstanceOf[Option[String]])
    }
  }

  def caseClassToParameterString(c: Product): String = {
    caseClassToMap(c).map(el =>
      if (el._2.isDefined) s"""${el._1}=${el._2.get}""" else ""
    ).filter(_ != "").mkString("&")
  }


  def sampleBarcodeToSuffix(sampleBarcode: String): Option[String] = {
    val parts = sampleBarcode.split("""\.""")
    if (parts.size > 1) {
      Some("." + parts.last)
    }
    else None
  }

  def fieldToList(field: String): List[String] = {
    field.split(AppConfig.Feed.listSplitter).toList
      .map {
        case "NA" | "N/A" | "None" => ""
        case s: String => s
      }
  }

  def fieldToOptList(field: String): Option[List[String]] = {
    field match {
      case "" | "NA" | "N/A" | "None" => None
      case _ => Some(fieldToList(field))
    }
  }

  // extracts list of pairs from a simple field:
  // ( txt1 : type1 + txt2 : type2 => List((txt1, type1), (txt2,type2))
  def fieldToOptPairList(field: String, omitFirstPartOnPairByDefault: Boolean = true):
  Option[List[(Option[String], Option[String])]] = {
    field.trim match {
      case "" | "NA" | "N/A" | "None" => None
      case _ => Some(fieldToList(field).map { p =>
        val pair = p.split(AppConfig.Feed.pairSplitter)
        Tuple2(
          if (pair.size > 1 || !omitFirstPartOnPairByDefault) stringToOption(pair.head.trim)
          else None,
          if (pair.size > 1 || omitFirstPartOnPairByDefault) stringToOption(pair.last.trim)
          else None
        )
      })
    }
  }
}
