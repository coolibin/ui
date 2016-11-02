package scenarios

import model._
import org.testng.annotations.DataProvider

class Sample1 extends TestBase {

  @Test(dataProvider = "testData")
  def sampleTest1(testData: Sample1CSV) = {
    log.debug(s"Running test ${testData.id}, ${testData.title}...")
    assert(true, "Test failed")
  }


  @Test(enabled = false)
  def testDataProvider() = {
    for (d <- testData) {
      log.debug(s"${d(0).id}, ${d(0).title},  ${d(0).data}")
    }
  }

  @DataProvider
  def testData: Array[Array[Sample1CSV]] = {
    loadFromCSV[Sample1CSV]("Sample1.csv").map(Array(_)).toArray
  }
}
