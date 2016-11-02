package ui

import java.net.URL
import java.util.concurrent.TimeUnit
import java.util.logging.Level

import common.{AppConfig, LogRunner}
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.{ChromeDriver, ChromeOptions}
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.remote.{DesiredCapabilities, RemoteWebDriver}

class Drivers extends LogRunner {

  val appConfig = AppConfig

  def getNewDriver(): WebDriver = {

    val options = new ChromeOptions()
    options.addArguments("--start-maximized") // for maximized

    val driver =
      appConfig.WebDriver.driverName.toLowerCase match {

        case "chrome" =>
          if (appConfig.WebDriver.runLocally)
            new ChromeDriver(options)
          else
            new RemoteWebDriver(new URL(appConfig.WebDriver.selHubURL), DesiredCapabilities.chrome())

        case "firefox" =>
          if (appConfig.WebDriver.runLocally)
              new FirefoxDriver()
          else
            new RemoteWebDriver(new URL(appConfig.WebDriver.selHubURL), DesiredCapabilities.firefox())

        case _ => new ChromeDriver(options)
      }

    driver.manage.timeouts.implicitlyWait(10, TimeUnit.SECONDS)

    if (appConfig.WebDriver.includeSeleniumLog) {
      driver.setLogLevel(Level.INFO)
    }

    log.debug("WebDriver initiated: " + driver.getClass.getName)
    driver.manage().window().maximize()
    driver
  }
}

object Drivers {
  def apply() = new Drivers
}
