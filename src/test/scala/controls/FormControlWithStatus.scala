package controls

import org.openqa.selenium.By

class FormControlWithStatus [P <: PBase](parentPage: P, locator: By, fieldName: String)
  extends FormControl(parentPage: P, locator: By, fieldName: String) with LogRunner {


  def hasSuccess: Boolean = {
    status == WebFormControlStatus.OK
  }

  def hasError: Boolean = {
    status == WebFormControlStatus.ERROR
  }

  def hasWarning: Boolean = {
    status == WebFormControlStatus.WARNING
  }

  def hasNoError: Boolean = {
    !hasError
  }

  def status: WebFormControlStatus.Status = {
    WebFormControlStatus.UNKNOWN // override in a child class!
  }
}
