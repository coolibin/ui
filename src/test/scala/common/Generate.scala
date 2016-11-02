package common

import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}

import scala.util.Random

object Generate {

  def nextNumber(i: Int = 0): String = DateTime.now.getMillis.toString + i.toString

  def nextRandom(): String = Random.nextInt().toString

  /**
    * Returns a string representing the date
    * calculated from current date by date offset
    *
    * @param dateOffset - number of days in past from the current date
    * @return
    */
  def drawDate(dateOffset: Int): String = {
    val drawDate = DateTime.now().minusDays(dateOffset)
    def fmt: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd")
    fmt.print(drawDate)
  }

  def drawDate: String = {
    drawDate(1) // default - yesterday date
  }
}
