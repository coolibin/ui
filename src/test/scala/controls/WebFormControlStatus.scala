package controls

/**
 *
 * Reflects status of the form field on the web page
 */
object WebFormControlStatus {

  sealed abstract class Status(val statusName: String)

  case object UNKNOWN extends Status("UNKNOWN")

  case object OK extends Status("OK")

  case object WARNING extends Status("WARNING")

  case object ERROR extends Status("ERROR")

  def mkFormControlStatus(statusName: String): Status = {
    statusName.toUpperCase match {
      case "WARNING" | "W" => WARNING
      case "ERROR" | "ERR" | "E" => ERROR
      case "OK" => OK

      // by partial class name on accessioning page
      case clName if clName.contains("IGBERR") => ERROR
      case clName if clName.contains("IGBERROR") => ERROR
      case clName if clName.contains("IGBSUCCESS") => OK
      case clName if clName.contains("IGBWARNING") => WARNING

      case _ => UNKNOWN
    }
  }
}
