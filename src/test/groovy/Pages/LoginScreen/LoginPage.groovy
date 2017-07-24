package Pages.LoginScreen

import Module.LoginFormModule
import geb.Page

/**
 * Created by pkurbetti on 7/22/2017.
 */
class LoginPage extends Page {
  static at = { $('.wpcom-site') }
  static content = {
    loginModule { $('.wpcom-site').module(LoginFormModule)}
  }

  def login(String username, String password) {
    loginModule.usernameInput = username
    loginModule.passwordInput = password
    loginModule.loginButton
  }
}
