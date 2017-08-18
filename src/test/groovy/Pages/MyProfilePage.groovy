package Pages

import Module.LoginFormModule
import geb.Page

/**
 * Created by vvenugopal on 8/3/2017.
 */
class MyProfilePage extends Page implements HelperMyProfilePage {


  static at = { content1.displayed }
  static content = {
    content1 { $('#content')}
    firstName { $('#first_name')}
    lastName { $('#last_name')}
    submitButton { $('#primary').find('button')[0]}
  }
}
