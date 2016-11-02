package controls

import org.openqa.selenium.By

class FormControlClickable[P <: PBase](parentPage: P, locator: By, fieldName: String)
  extends FormControl(parentPage: P, locator: By, fieldName: String) {

  def click(doClick: Boolean = true, timeLimitSec: Int = 2): P = {
    if (doClick) parentPage.clickWhenClickable(locator, fieldName)
    parentPage
  }

  def click(doClickOpt: Option[Boolean]): P = {
    click(doClickOpt.getOrElse(false))
  }
}
