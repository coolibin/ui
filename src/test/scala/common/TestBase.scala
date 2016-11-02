package common

import java.lang.reflect.Method

import org.hamcrest.MatcherAssert._
import org.hamcrest.Matchers._
import org.testng.ITestResult
import org.testng.annotations._

trait TestBase extends LogRunner
//  with org.testng.ITest
  with CSVRunner
  with DBRunner {

//  var testRef: TestRef = null


  @BeforeSuite
  def beforeSuite = {
    log.info("""Running Test Suite""")
  }

  @BeforeClass
  def beforeClass = {

  }

  @AfterClass
  def afterClass = {
  }

  @BeforeTest
  def beforeTest = {
  }

  @BeforeMethod
  def beforeMethod(method: Method /*, testData: Array[Object]*/) = {
    log.debug(">>>>>>>>>>>")
    log.info("STARTED: " + method.getName)
  }

  @AfterMethod
  def afterMethod(result: ITestResult) = {
    log.info("Method {} FINISHED with status = {}{}",
      result.getMethod.getMethodName,
      this.testResultStatus(result.getStatus), "")
  }

  /**
    * Return a text representation of the ITestResult.ENUM
    *
    * @param status
    * @return
    */
  def testResultStatus(status: Int): String = {
    status match {
      case ITestResult.SUCCESS => "PASS"
      case ITestResult.FAILURE => "FAIL"
      case ITestResult.SKIP => "SKIP"
      case ITestResult.SUCCESS_PERCENTAGE_FAILURE => "SUCCESS_PERCENTAGE_FAILURE"
      case _ => ""
    }
  }

  def validateFieldWithDB[T](fieldName: String, field: Option[T], dbValue: Option[T]) = {
    if (field.isDefined) {
      log.trace(s"Validating field $fieldName...")
      assertThat(s"$fieldName should be equal to a db value", field.get, equalTo(dbValue.get))
    }
  }

  def validateFieldWithDB[T](fieldName: String, field: T, dbValue: T) = {
    log.trace(s"Validating field $fieldName...")
    assertThat(s"$fieldName should be equal to a db value", field, equalTo(dbValue))
  }
}
