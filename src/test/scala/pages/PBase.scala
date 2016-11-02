package pages

import common.{LogRunner, SeleniumRunner}
import org.openqa.selenium.{By, WebElement}

class PBase(pManager: PManager) extends SeleniumRunner
  with LogRunner {

  def url: String = ""
  var pageManager: PManager = pManager
  driver = pageManager.driver

  protected def navigateTo(url: String) = {
    log.debug("Navigate to {}", url)
    driver.get(url)
  }

  def goTo(): this.type = {
    navigateTo(url)
    this
  }

  def goTo(additionalParam: String): this.type ={
    navigateTo(url)
    this
  }

  def parentElement(webElement: WebElement): WebElement = {
    webElement.findElement(By.xpath(".."))
  }
}
