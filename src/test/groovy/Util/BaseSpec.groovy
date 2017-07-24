package Util

import Pages.LoginScreen.LoginPage
import Pages.WordPressWelcomeScreen.WordPressWelComePage
import Util.UserModel.UserModel
import annotation.Login
import com.saucelabs.saucerest.SauceREST
import geb.Browser
import geb.navigator.Navigator
import geb.report.ScreenshotReporter
import geb.spock.GebSpec
import geb.waiting.WaitTimeoutException
import org.json.JSONObject
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriverException
import org.openqa.selenium.interactions.Actions
import spock.lang.Shared

/**
 * Created by pkurbetti on 7/19/2017.
 */
abstract class BaseSpec extends GebSpec  {
  /**
   * To add a system property:
   *   * With eclipse or IntelliJ: run configuration -> arguments -> vm arguments
   *     add -Dkey=value
   *     ex: -Dbase.lss.default.sitecode = 0SLW0SLW
   *
   *   * With maven: add -Dkey=value after mvn test
   */
  // To replace URL: -Dbase.URL = URL
  static propBaseUrl = "base.URL"
  static propSaucelabUser = "geb.saucelabs.user"
  static propSaucelabKey = "geb.saucelabs.key"
  static propJobName = "jobName"
  static propLssConfigfile = "base.lss.configfile"
  static String propLss = "base.lss"
  static propLsslocal = "base.lss.local"
  static Browser browserInstance
  static givenBaseUrl
  static _baseUrl
  @Shared baseUrl
  static gebconfig

  static onSauceLabs = false
  static SauceREST sauceClient
  static sauceJobID = ""
  @Shared UserModel userModel = new UserModel()

  def setup() {
    String featureName = getSpecificationContext()?.getCurrentIteration()?.getName()
    println ("Init test : ") + "$featureName [$browser]"
    report "======= Feature here $featureName ====="
  }

  def setupSpec() {
    // To improve DB access, we need to add a static field
    // because we don't want to connect to DB each time call getBaseUrl function
    String realImplClassName = getClass().getSimpleName()
    println "Init class: $realImplClassName"
    gebconfig = config.rawConfig
    givenBaseUrl = CredentialUtils.getTestProperty(propBaseUrl, (String)gebconfig.defaultBaseUrl)
    //Report setting
    browserInstance = getBrowser()
    addCommonNavigatorMethods()
    addCommonStringMethods()
    reportGroup this.class.name
    cleanReportGroupDir()
    initDriver()

    //start ghosting
    setup: "Login to url"
      if (this.class.isAnnotationPresent(Login)) {
        def lgn = this.class.getAnnotation(Login)
        // use Helper.getSitecode() to get sitecode based on LSS key
        loadLoginPage()
        // Login into SECO with DB or system property information
        userModel.username = lgn.username() == "" ? CredentialUtils.getUsername(lgn.lssuser()) : lgn.username()
        userModel.password = lgn.password() == "" ? CredentialUtils.getPassword(lgn.lssuser()) : lgn.password()
//        userModel.emulateUsername = System.getProperty(propEmulateUsername, lgn.emulateUsername())
//        userModel.emulateOfficeid = System.getProperty(propEmulateOfficeid, lgn.emulateOfficeid())
        this.loginToMainPage(userModel)
      }
  }

  def cleanup() {
    String realImplClassName = getClass().getSimpleName()
    println("Cleanup class:") + "$realImplClassName"
  }

  void initDriver() {
    onSauceLabs = System.getProperty("geb.env") == "saucelabs"
    getBrowser().getConfig().setReporter(new GebReporter(new PageSourceWithBaseReporter(), new ScreenshotReporter()))

    if (onSauceLabs) {
      // Set test name
      def sauceUsername = System.getProperty(propSaucelabUser, (String)gebconfig.USERNAME)
      def sauceAccessKey = System.getProperty(propSaucelabKey, (String)gebconfig.AUTHKEY)

      sauceJobID = driver.getSessionId().toString()
      sauceClient = new SauceREST(sauceUsername, sauceAccessKey)
      def jobname = System.getProperty(propJobName, "URL " + gebconfig.release)

      def additionalTag = System.getProperty(jobname)
      def tags = additionalTag ? [gebconfig.release].push(additionalTag) : [gebconfig.release]

      sauceClient.updateJobInfo(sauceJobID, [name: jobname + " - " + this.class.simpleName, tags:tags])

      // Store direct link to saucelabs report
      def file = new File(getBrowser().getReportGroupDir(), 'SauceLabs.html')
      file << "<meta http-equiv=\"refresh\" content=\"0; url=https://saucelabs.com/tests/${sauceJobID}\" />"
    }
  }

  /**
   * Load SECO login page with sitecode and URL parameters
   * @param sitecode sitecode, ex: 0slw0slw
   * @param params URL parameters, ex: ["OCTX=UAT","OV_SITE_ENABLE_QM=FALSE"]
   */
  void loadLoginPage() {
    //secoUrl = getBaseUrl() + "/app_sell2.0/apf/init/login?LANGUAGE=$language" + inputParam("SITE", sitecode)
    givenBaseUrl = getBaseUrl()
    //  log.info "Load URL: <div id='testedURL' > ${secoUrl} </div>"
    browser.config.driver?.capabilities.with { println "Browser version: $browserName $version" }
    println "Load URL: <div id='testedURL' > ${givenBaseUrl} </div>"

    go givenBaseUrl
    at WordPressWelComePage
    report "Welcome page"
    println "Load URL: <div id='testedURL' > ${givenBaseUrl} </div>"
    def file = new File(getBrowser().getReportGroupDir(), 'URL.html')
    file << "<meta http-equiv=\"refresh\" content=\"0; url=${givenBaseUrl}\" />"
  }

  void loadAndLogin() {
    this.loadLoginPage()
    this.loginToMainPage(userModel)
  }

  /**
   * Login into SECO
   * By default, isDefaultOffice = true, emulateUsername = "" and emulateOfficeid = ""
   * if is DefaultOffice = false, test will force to login into the given officeid
   * [Feature emulate user] if &HELPDESK=1A is in SECO URL, emulateUsername and emulateOfficeid is necessary
   *
   * @param userModel Contains all the LSS information required to login or emulate users
   */
  // TODO add isDefaultOffice in DB or not?
  void loginToMainPage(UserModel userModel) {
//    def isEmulateUser = baseUrl.contains('HELPDESK=1A')
//    if (isEmulateUser) {
////      loginWithEmulateUser (userModel.username, userModel.password,
////        userModel.emulateUsername, userModel.emulateOfficeid)
//    } else {
      at WordPressWelComePage
      loginLink.click()
      at LoginPage
      login(userModel.username, userModel.password)
    //}
  }

  void oneAfterAnother(Closure definition) {
    OneAfterAnother.oneAfterAnother(this, definition)
  }

  // This has only be created for easySelenium and should not be used manually
  def assertContains(String origin, String expected) {
    def result = origin.contains(expected)
    if (!result) {
      report "ERROR"
    }
    result
  }

  def cleanupSpec() {
    // Gather and log javascript errors
    JsHelper.saveConsole(browser)

    // Report JsessionIDs in a file
    def jsessionIDs = JsHelper.saveJSessionIds(browser)
    if (jsessionIDs && onSauceLabs) {
      JSONObject metadata = new JSONObject()
      metadata.put("JSESSIONID", jsessionIDs)
      println "jsessionIDs: ${jsessionIDs}"
      sauceClient.updateJobInfo(sauceJobID, ["custom-data": metadata])
    }

    JsHelper.removeAlert(browser)
    if (onSauceLabs) {
      // Need to quit the driver explicitely to force new session on SauceLabs
      driver.quit()
    } else {
      go "about:blank"
    }
  }

  void resetBrowser() {
    // Overridden to not reset the browser
  }

  def methodMissing(String name, args) {
    try {
      Helper."$name"(* args)
    } catch (MissingMethodException e) {
      getBrowser()."$name"(* args)
    }
  }

  /**
   * Mouse over an element.
   * @param elt the element
   */
  public void mouseOver(Navigator elt) {
    new Actions(driver).moveToElement(elt.firstElement()).perform()
  }

  /**
   * Wait a refresh and the end of this refresh.
   */
  public void waitARefresh() {
    def refresh = true
    try {
      waitFor('quick') {
        $(".uicLo-loading, body > div.xLDI, .alertColumn.loading").any {
          it.displayed
        }
      }
    } catch ( WaitTimeoutException we) {
      //The refresh has already happened
      refresh = false
    }
    if (refresh) {
      waitFor {
        $(".uicLo-loading, body > div.xLDI, .alertColumn.loading").every {
          !it.displayed
        }
      }
    }
  }

  private void addCommonNavigatorMethods() {
    Navigator.metaClass.wait_and_click = { ->
      try {
        delegate.click()
      } catch ( WebDriverException e) {
        if (e.message.contains("Element is not clickable")) {
          report "before second click"
          sleep 1000
          waitFor {
            !$(".uicLo-loading, body > div.xLDI, .alertColumn.loading").displayed &&
              !$("body > div.xLDI").displayed &&
              !$(".uicLo-loading").displayed
          }
          report "before second click"
          delegate.click()
        }
        else
          throw e
      }
    }

    Navigator.metaClass.isReadOnly = { ->
      delegate.attr('readonly') == 'true'
    }

    //more utility methods can be added hereafter
  }

  def fillAutoComplete(autoComplete, value) {
    autoComplete.value(value)
    // simulate blur to trigger DOM updates
    autoComplete << Keys.TAB
  }

  private void addCommonStringMethods() {
    String.metaClass.indexesOf = { match ->
      def ret = []
      if (match) {
        def idx = -1
        while ((idx = delegate.indexOf(match, idx + 1)) > -1) {
          ret << idx
        }
      }
      ret
    }

    //more string methods can be added hereafter
  }

  /**
   * Method to switch to the browser pop up invoked
   * sleep required, since its a browser pop up.
   * @return
   */
  def switchtoBrowserPopupWindow() {
    //sleep is needed since a new browser pop up is invoked
    sleep(5000)
    for (String winHandle : browser.driver.getWindowHandles()) {
      browser.driver.switchTo().window(winHandle)
    }
  }

  /**
   * Method to get current parent window
   * @return: String parentWindow
   */
  def getParentWindow () {
    browser.driver.getWindowHandle()
  }

  /**
   * Method to switch back to the parent window
   * @param parentWindow : String instance of the parent window(browser)
   * @return
   */
  def switchBackToParentWindow (String parentWindow) {
    browser.driver.switchTo().window(parentWindow)
  }

  /**
   * Method to get the current url
   * @return: String url
   */
  def getCurrentURL () {
    browser.driver.getCurrentUrl()
  }

  /**
   * Perform an action with arguments in an opened window modality smart tool <br>
   * @param winName : window name of the smart tool window <br>
   * @param method : name of the method which will be performed in the window <br>
   * @return: <br>
   */
  def invokeMethodInWindow(winName, method, args) {
    withWindow(winName) {
      method(args)
    }
  }
}

