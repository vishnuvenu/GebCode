package Module

import geb.Module

/**
 * Created by pkurbetti on 7/22/2017.
 */
class LoginFormModule extends Module {
  static content = {
    usernameInput { $('#usernameOrEmail') }
    passwordInput { $('#password') }
    loginButton { $('.login__form-action button') }
  }
}
