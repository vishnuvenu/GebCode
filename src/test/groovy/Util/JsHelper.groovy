package Util

import geb.Browser

/**
 * @author FOR-SEC-SWU
 */
class JsHelper {

  public static getjSessionId(Browser browser) {
    try {
      browser.js.exec("""var jsessionid = {}
  if (a != undefined) {
    for (vs in a.shell.viewsets) {
      if (typeof a.shell.viewsets[vs] == "object" && vs !== "body") {
        var v = a.shell.viewsets[vs];
        jsessionid[v.title] = v.jsessionid;
        if (v.bTools != undefined) {
          for (tool in v.bTools) {
            if (v.bTools[tool].initialized == true) {
              jsessionid[v.bTools[tool].title] = v.bTools[tool].jsessionId
            }
          }
        }
      }
    }
  }
  return JSON.stringify(jsessionid, null, " ");
""") }
    catch (e) { print(e) }
  }

  public static void saveJSessionIds(Browser browser) {
    def reportDir = browser.getReportGroupDir()
    def jsessionIDs = getjSessionId(browser)
    if (jsessionIDs) {
      println "jsessionIDs : <div id='jsessionIDs'> ${jsessionIDs} </div>"
      def file = new File(reportDir, 'JSESSION.log')
      file << jsessionIDs
    }
    jsessionIDs
  }

  static void removeAlert(browser) {
    browser.driver.executeScript("try{c.remEvent(window,'beforeunload')}catch(e){}")
  }

  static void saveConsole(Browser browser) {
    def reportDir = browser.getReportGroupDir()
    def jsErrors = browser.js."window.jsErrors"
    if (jsErrors) {
      def file = new File(reportDir, 'jsErrors.log')
      file << jsErrors.join("\n") + "\nEND\n"
      println jsErrors.join("\n")
    }

    def jsConsole = browser.js."window.jsLogs"
    if (jsConsole) {
      def file = new File(reportDir, 'jsConsole.log')
      file << jsConsole.join("\n") + "\nEND\n"
    }
  }
}
