package common

import java.lang.reflect.Method

import org.testng.ITestResult
import org.testng.annotations.{AfterMethod, BeforeMethod}
import pages.PManager

trait TestBaseUI extends TestBase with SeleniumRunner {

  var pageManager: PManager = null

  @BeforeMethod
  override def beforeMethod(method: Method /*, testData: Array[Object]*/) = {
    super.beforeMethod(method)
    this.initDriver
    pageManager = PManager(driver)
  }

  @AfterMethod
  override def afterMethod(result: ITestResult) = {
    super.afterMethod(result)

    if (result.getStatus == ITestResult.FAILURE) {
      takeScreenShot(s"screenshot_${scala.util.Random.nextInt()}.png")
    }

    log.debug("<<<<<<<<<<")

    //TODO: add support for TestRail reporting
    this.closeDriver
  }
}
