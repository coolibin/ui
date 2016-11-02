package ui

import java.io.{File, IOException}

import common.{AppConfig, LogRunner}
import org.apache.commons.io.FileUtils
import org.openqa.selenium.{OutputType, TakesScreenshot, WebDriver}

object Screenshot extends LogRunner {
  @throws[IOException]
  def takeScreenshot(driver: WebDriver, fileName: String) {
    val ssFolder: String = AppConfig.WebDriver.screenshotFolder
    val scrFile: File = driver.asInstanceOf[TakesScreenshot].getScreenshotAs(OutputType.FILE)
    log.debug(s"Saving a screenshot to ${ssFolder + fileName}")
    FileUtils.copyFile(scrFile, new File(ssFolder + fileName))
  }
}
