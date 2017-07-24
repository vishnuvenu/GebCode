package Pages.WordPressWelcomeScreen

import Util.BaseSpec
import annotation.Login
import geb.Page

/**
 * Created by pkurbetti on 7/22/2017.
 */
@Login(username = "", password = "")
class WordPressWelComePage extends Page {

  static at = { $('#wpcom-home') }

  static content = {
    loginLink { $('#navbar-login-link') }
  }

}
