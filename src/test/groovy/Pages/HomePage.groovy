package Pages

import Module.LoginFormModule
import geb.Page

/**
 * Created by vvenugopal on 8/3/2017.
 */
class HomePage extends Page implements HelperHomePage {

  static at = { headerBar.displayed }
  static content = {
    headerBar { $('#header')}
    loginModule { $('.wpcom-site').module(LoginFormModule)}
    itemMe (wait:true){ headerBar.find('a')[3]}
  }
}
