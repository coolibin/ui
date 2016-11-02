package pages

import common.AppConfig
import controls._

class PLogin(pageManager: PManager) extends PBase(pageManager) {

  override def url = AppConfig.UI.baseURL + AppConfig.Login.url

  val loc = PLoginLocators

  lazy val userNameCtrl = new FormControlText(this, loc.userNameField, "UserName").withTimeLimit(10)
  lazy val userPasswordCtrl = new FormControlPassword(this, loc.passwordField, "UserPassword")
  lazy val loginBtn = new FormControlClickable(this, loc.loginButton, "Submit")

  def typeUsername(username: String): this.type = {
    userNameCtrl.typeText(username)
    this
  }

  def typePassword(password: String): this.type = {
    userPasswordCtrl.typeText(password)
    this
  }

  def submitLogin(): this.type = {
    loginBtn.click()
    Thread.sleep(2000) //To make sure that token saved in db
    this
  }

  def getUserNameText: String = {
    waitForElement(_.findElement(loc.userNameField)).getAttribute("value")
  }

  def ensurePageLoaded() = {
    log.debug("Making sure page is loaded...")
    waitForElementClickable(loc.userNameField, 10)
    this
  }

  def getLoggedUserName: String = {
    waitForElement(_.findElement(loc.curUserName), 10).getText
    //todo refactor to avoid delay on error
  }

  def loginAsDefault = loginAs(AppConfig.Login.username, AppConfig.Login.password)

  def loginAs(username: String, password: String) = {

    log.debug("Log in as " + username + " with password=*******")

    var pageLogin = this.goTo()

    try pageLogin.ensurePageLoaded()
    catch {
      case _: Throwable =>
        log.debug("Login dialog did not appear from the first attempt, will try one more time... ")
        pageLogin = this.goTo().ensurePageLoaded()
    }

    pageLogin.typeUsername(username).typePassword(password).submitLogin()

    log.debug("Logged in as " + getLoggedUserName)
    this
  }
}
