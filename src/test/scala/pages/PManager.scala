package pages

import org.openqa.selenium.WebDriver

class PManager(var driver: WebDriver) {

  // list all existing pages here
  val pageLogin: PLogin = new PLogin(this)
//  val pageAccessioningCS2 = new PAccessioningHZ(this)
//  val pageAccessioningCS2Combo = new PAccessioningHZCombo(this)
//  val pageAccessioningNPT = new PAccessioningNPT(this)
//  val pageExtranetReferralHZ = new PExtranetReferralHZ(this)
//  val pageExtranetLogin = new PExtranetLogin(this)
//  val pageExtranetParentKit = new PExtranetParentKitHZ(this)
//  val pageLimsLogin = new PLimsLogin(this)
//  val pageLimsParentKitHZ = new PLimsParentKitHZ(this)
}

object PManager {
  def apply(driver: WebDriver) = new PManager(driver)
}

