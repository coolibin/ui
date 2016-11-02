import common.AppConfig
import org.scalatest.testng.TestNGWrapperSuite

class TestRunner extends TestNGWrapperSuite (
  List(AppConfig.autoRun)
)