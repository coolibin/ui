package controls

import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.{By, WebElement}

abstract class FormControl [P <: PBase](parentPage: P, locator: By, fieldName: String) extends LogRunner {

  protected var timeLimitSec: Int = 2

  def withTimeLimit(timeLimitSec: Int): this.type = {
    this.timeLimitSec = timeLimitSec
    this
  }

  def moveTo(): P = {
    parentPage.moveToElement(locator)
    parentPage
  }

  def mouseHover(): P = {
    log.debug(s"Mouse hover at: $fieldName")
    val builder: Actions = new Actions(parentPage.driver)
    builder.moveToElement(webElement).perform()
    Thread.sleep(100)
    parentPage
  }

  def isVisible: Boolean = {
    parentPage.elementIsVisible(locator, 1)
  }

  def webElement: WebElement = parentPage.driver.findElement(this.locator)
}
