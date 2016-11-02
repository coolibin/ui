package controls

import org.openqa.selenium.By

class FormControlInputBox[P <: PBase](parentPage: P, locator: By, fieldName: String)
  extends FormControlWithStatus(parentPage: P, locator: By, fieldName: String) with LogRunner {

  override def status: WebFormControlStatus.Status = {

    val containerClassAtribute = webElement.getAttribute("class")
    WebFormControlStatus.mkFormControlStatus(containerClassAtribute)
  }
}
