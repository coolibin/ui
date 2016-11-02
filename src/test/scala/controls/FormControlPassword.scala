package controls

import org.openqa.selenium.{By, Keys}

class FormControlPassword[P <: PBase](parentPage: P, locator: By, fieldName: String)
  extends FormControlText(parentPage: P, locator: By, fieldName: String)
    with LogRunner {

  override def typeText(text: String): P = {
    if (text.nonEmpty) {
      log.debug("Enter password: " + "*" * text.length)
      parentPage.waitForElementClickable(locator, this.timeLimitSec * 1000L).sendKeys(text)
    }
    parentPage
  }

  override def typeText(textOpt: Option[String]): P = {
    typeText(textOpt.getOrElse(""))
  }

  override def typeTextWithConfirm(text: String): P = {
    if (text.nonEmpty) {
      parentPage.waitForElementClickable(locator, this.timeLimitSec * 1000L).sendKeys(text + Keys.ENTER)
    }
    parentPage
  }

  override def typeTextWithConfirm(textOpt: Option[String]): P = {
    typeTextWithConfirm(textOpt.getOrElse(""))
  }

  override def getText(maxWaitSec: Int = 5): String = {
    "*" * parentPage.waitForElement(_.findElement(locator), maxWaitSec).getAttribute("value").length
  }
}
