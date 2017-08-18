package Test

import Pages.HomePage
import Pages.MyProfilePage
import Util.BaseSpec
import annotation.DataDriven
import annotation.Login
import spock.lang.Unroll

/**
 * Created by vvenugopal on 8/04/2017.
 */

@DataDriven
@Login(username = 'vishnuvenugopal73@gmail.com', password = 'Chase123$$')
class Validation_DataDriven_Spec extends BaseSpec {

@Unroll

  def 'validate  #fname #lname' () {
    given: 'At welcome page'
      at HomePage
    when: 'move to profile page'
      goToMyProfile()
    then: ''
      at MyProfilePage

    when:''
    updateNames(fName,lName)
    then:''
    at MyProfilePage

    where:

    fName | lName
    "Fname1" | "lName1"
    "Fname2" | "lName2"
    "Fname3" | "lName3"


  }

  def cleanup() {
    print("Test1")
  }
}
