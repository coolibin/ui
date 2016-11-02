package pages

import org.openqa.selenium.By

/**
  * Created by akalinichev on 9/6/16.
  */
object PLoginLocators {
  val userNameField: By = By.id("j_username")
  val passwordField: By = By.id("j_password")
  val loginButton: By = By.cssSelector("button.btn.btn-primary")
  val curUserName: By = By.xpath("//a[contains(text(), 'Logged')]/span")
}
