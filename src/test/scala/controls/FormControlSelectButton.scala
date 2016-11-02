package controls

import org.openqa.selenium.By

class FormControlSelectButton[P <: PBase](parentPage: P, locator: By, fieldName: String)
  extends FormControlClickable(parentPage: P, locator: By, fieldName: String)
    with LogRunner {

  def isSelected: Boolean = {

    try parentPage.waitForElement(_.findElement(locator), this.timeLimitSec).getAttribute("class").contains("active")
    catch {
      case _: Throwable =>
        log.debug("Could not get a status of checkbox... ")
        false
    }
  }


}
