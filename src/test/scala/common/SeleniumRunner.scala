package common

import java.util.concurrent.TimeUnit

import org.openqa.selenium._
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.{ExpectedCondition, ExpectedConditions, FluentWait, WebDriverWait}
import ui.{Drivers, Screenshot}

import scala.collection.JavaConversions._

trait SeleniumRunner extends LogRunner {
  var driver: WebDriver = null
  var defaultWaitTimeoutMillis = 20000L
  //TODO make it a config parameter
  var defaultWaitPollingMillis = 100L //TODO make it a config parameter

  def initDriver: Unit = {
    driver = Drivers().getNewDriver()
  }

  def closeDriver: Unit = {
    if (driver != null) {
      driver.quit()
    }
  }

  def initFluentWait(waitTimeoutMillis: Long, waitPollingMillis: Long): Unit = {
    defaultWaitTimeoutMillis = waitTimeoutMillis
    defaultWaitPollingMillis = waitPollingMillis
  }

  def fluentWait =
    new FluentWait[WebDriver](driver)
      .withTimeout(defaultWaitTimeoutMillis, TimeUnit.MILLISECONDS)
      .pollingEvery(defaultWaitPollingMillis, TimeUnit.MILLISECONDS)

  def fluentWait(timeOutMillis: Long = 20000, pullingMillis: Long = 100) =
    new FluentWait[WebDriver](driver)
      .withTimeout(timeOutMillis, TimeUnit.MILLISECONDS)
      .pollingEvery(pullingMillis, TimeUnit.MILLISECONDS)

  def waitForElementClickable(locator: By): WebElement = {
    fluentWait.until(ExpectedConditions.elementToBeClickable(locator))
  }

  def waitForElementClickable(locator: By, timeLimitSec: Long): WebElement = {
    fluentWait(timeLimitSec * 1000L, 1000L).until(ExpectedConditions.elementToBeClickable(locator))
  }

  def waitForElementClickable(element: WebElement, timeLimitSec: Long): WebElement = {
    fluentWait(timeLimitSec * 1000L, 1000L).until(ExpectedConditions.elementToBeClickable(element))
  }

  def waitForElementClickable(element: WebElement): WebElement = {
    fluentWait.until(ExpectedConditions.elementToBeClickable(element))
  }

  def clickWhenClickable(element: WebElement): Unit = {
    waitForElementClickable(element).click()
  }

  def clickWhenClickable(locator: By, timeLimitSec: Long = 5L): Unit = {
    waitForElementClickable(locator, timeLimitSec).click()
  }

  def clickWhenClickable(locator: By, fieldName: String): Unit = {
    recordLogClickElement(fieldName)
    clickWhenClickable(locator)
  }

  def waitForElementToBeSelected(locator: By) {
    fluentWait.until(ExpectedConditions.elementToBeSelected(locator))
  }

  def waitForElementToBeSelected(element: WebElement) {
    fluentWait.until(ExpectedConditions.elementToBeSelected(element))
  }

  def waitForElementDisplayed(element: WebElement) {
    fluentWait.until(ExpectedConditions.visibilityOf(element))
  }

  def waitForAttributeToBe(locator: By, attribute: String, value: String) {
    fluentWait.until(ExpectedConditions.attributeToBe(locator, attribute, value))
  }

  def waitForAttributeToBe(element: WebElement, attribute: String, value: String) {
    fluentWait.until(ExpectedConditions.attributeToBe(element, attribute, value))
  }

  def typeWhenClickable(locator: By, text: String, fieldName: String = "", timeLimitSec: Int = 10) = {
    if (text.nonEmpty) {
      recordLogEnterField(fieldName, text)
      //TODO: make it a config param
      waitForElementClickable(locator, timeLimitSec * 1000L).sendKeys(text)
    }
  }

  def typeWhenClickable(element: WebElement, text: String, fieldName: String) = {
    if (text.nonEmpty) {
      recordLogEnterField(fieldName, text)
      waitForElementClickable(element, 1000L).sendKeys(text)
    }
  }

  def waitForElement(f: (WebDriver) => WebElement): WebElement = {
    waitForElement(f, (defaultWaitTimeoutMillis / 1000L).asInstanceOf[Int])
  }

  def waitForElement(f: (WebDriver) => WebElement, timeLimitSec: Int): WebElement = {
    new WebDriverWait(driver, timeLimitSec)
      .until(new ExpectedCondition[WebElement] {
        override def apply(d: WebDriver) = f(d)
      })
  }

  def elementIsVisible(locator: By, timeLimitSec: Int = 1): Boolean = {
    try {
      waitForElement(_.findElement(locator), timeLimitSec).isDisplayed
    } catch {
      case e: TimeoutException => false
    }
  }

  // waits during the time limit specified until element turns invisible
  def hasElementDisappeared(locator: By, timeLimitSec: Int = 10): Boolean = {
    new WebDriverWait(driver, timeLimitSec).until(ExpectedConditions.invisibilityOfElementLocated(locator))
  }

  @Deprecated
  def waitForElement(driver: WebDriver, f: (WebDriver) => WebElement): WebElement = {
    new WebDriverWait(driver, defaultWaitTimeoutMillis / 1000L).until(
      new ExpectedCondition[WebElement] {
        override def apply(d: WebDriver) = f(d)
      })
  }

  @Deprecated
  def waitForTime(driver: WebDriver, f: (WebDriver) => WebElement, timeLimitSec: Int): WebElement = {
    new WebDriverWait(driver, defaultWaitTimeoutMillis / 1000L)
      .until(new ExpectedCondition[WebElement] {
        override def apply(d: WebDriver) = f(d)
      })
  }

  protected def recordLogEnterField(fieldName: String, fieldValue: String) = {
    if (fieldName.nonEmpty) {
      log.debug(s"Enter '$fieldName': $fieldValue")
    }
  }

  protected def recordLogClickElement(elementName: String) = {
    log.debug(s"Click '$elementName'")
  }

  //TODO: check direction!!!
  def scrollDown(px: Int) = {
    if (px > 0) {
      log.debug(s"Scrolling down ${px}px")
      driver.asInstanceOf[JavascriptExecutor].executeScript(s"scroll(0,-$px)")
    }
  }

  def scrollUp(px: Int) = {
    if (px > 0) {
      log.debug(s"Scrolling up ${px}px")
      driver.asInstanceOf[JavascriptExecutor].executeScript(s"scroll(0,$px)")
    }
  }

  def takeScreenShot(fileName: String) = {
    Screenshot.takeScreenshot(driver, fileName)
  }

  def saveHTMLto(fileName: String) = {
    val pageSource = driver.getPageSource
    FileManager.saveTextToFile(pageSource, fileName)
  }

  def moveToElement(element: WebElement): Unit = {
    val actions: Actions = new Actions(driver)
    actions.moveToElement(element)
    actions.perform()
  }

  def moveToElement(locator: By): Unit = {
    val element = waitForElement(_.findElement(locator))
    moveToElement(element)
  }

  def moveToElementAndClick(element: WebElement): Unit = {
    val actions: Actions = new Actions(driver)
    actions.moveToElement(element)
    actions.click()
    actions.perform()
  }

  def findElements(locator: By): List[WebElement] = {
    new WebDriverWait(driver, defaultWaitTimeoutMillis / 1000L).until(
      new ExpectedCondition[List[WebElement]] {
        override def apply(d: WebDriver) = d.findElements(locator).toList
      })
  }
}
