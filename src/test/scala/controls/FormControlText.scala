package controls

import org.openqa.selenium.{By, Keys}

class FormControlText[P <: PBase](parentPage: P, locator: By, fieldName: String)
  extends FormControlWithStatus(parentPage: P, locator: By, fieldName: String) with LogRunner {

  def clear(timeLimitSec: Int = this.timeLimitSec): P = {
    if (getText().nonEmpty) {
      parentPage.waitForElement(_.findElement(locator), timeLimitSec).clear()
    }
    parentPage
  }

  def typeText(text: String): P = {
    if (text.nonEmpty) {
      parentPage.typeWhenClickable(locator, text, fieldName, this.timeLimitSec)
    }
    parentPage
  }

  def typeText(textOpt: Option[String]): P = {
    typeText(textOpt.getOrElse(""))
  }

  def typeTextWithConfirm(text: String): P = {
    if (text.nonEmpty) {
      parentPage.typeWhenClickable(locator, text + Keys.ENTER, fieldName)
    }
    parentPage
  }

  def typeTextWithConfirm(textOpt: Option[String]): P = {
    typeTextWithConfirm(textOpt.getOrElse(""))
  }

  def getText(maxWaitSec: Int = 5): String = {
    parentPage.waitForElement(_.findElement(locator), maxWaitSec).getAttribute("value")
  }

  def isEnabled = {
    val el = parentPage.waitForElement(_.findElement(locator), 2)
    if (el != null) {
      !parentPage.parentElement(el)
        .getAttribute("class").contains("disabled")
    } else {
      false
    }
  }

  override def status: WebFormControlStatus.Status = {
    var containerClassAtribute = parentPage.parentElement(webElement).getAttribute("class")
    // for some input boxes it is one level upper
    if (containerClassAtribute.contains("twitter-typeahead"))
      containerClassAtribute = parentPage.parentElement(parentPage.parentElement(webElement)).getAttribute("class")

    WebFormControlStatus.mkFormControlStatus(containerClassAtribute)
  }
}
