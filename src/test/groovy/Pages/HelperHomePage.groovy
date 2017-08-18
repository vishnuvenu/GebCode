package Pages

/**
 * Created by vvenugopal on 8/3/2017.
 */
trait HelperHomePage {


  public HomePage homePage = this
  def goToMyProfile() {
    homePage.itemMe.click()
  }
}