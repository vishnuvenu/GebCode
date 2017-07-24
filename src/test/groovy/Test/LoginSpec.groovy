package Test

import Util.BaseSpec
import Util.UserModel.UserModel
import annotation.Login

/**
 * Created by pkurbetti on 7/22/2017.
 */
@Login(username = 'xyz@gmail.com', password = '123456')
class LoginSpec extends BaseSpec {
  def 'validate Login' () {
    given: 'At welcome page'
    when: ''
    then: ''
  }
}
