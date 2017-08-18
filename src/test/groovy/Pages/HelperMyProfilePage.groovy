package Pages

/**
 * Created by vvenugopal on 8/3/2017.
 */
trait HelperMyProfilePage {
public MyProfilePage myProfilePage = this

 def updateNames(String fName, String lName) {
   myProfilePage.firstName = ''
   myProfilePage.firstName = fName
   myProfilePage.lastName=''
   myProfilePage.lastName = lName
   myProfilePage.submitButton.click()
  }
}