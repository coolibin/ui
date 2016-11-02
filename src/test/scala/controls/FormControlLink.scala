package controls

import org.openqa.selenium.By

class FormControlLink [P <: PBase](parentPage: P, locator: By, fieldName: String)
  extends FormControlClickable(parentPage: P, locator: By, fieldName: String) with LogRunner{

  def getText(maxWaitSec: Int = 5): String = {
    parentPage.waitForElement(_.findElement(locator), maxWaitSec).getText
  }
}
