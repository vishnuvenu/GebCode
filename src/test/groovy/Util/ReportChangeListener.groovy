package Util

import geb.Browser
import geb.Page
import geb.PageChangeListener
import groovy.time.TimeCategory
import groovy.time.TimeDuration

/**
 * Logs are captured for the page change. <br>
 * currently the error recording mechanism may not work <br>
 * since we do not have generic locator for error or warning <br>
 *  - @maintainer R&D-ALB-RDM-FOR-QDS-ANR <br>
 */
class ReportChangeListener implements PageChangeListener {
  Date dateSinceLastChange  = new Date()
  
  void pageWillChange(Browser browser, Page oldPage, Page newPage) {
    
    if (newPage && !(newPage.getClass() == Page)) {
      // Automatic report (log + report)
      Date changeDate = new Date()
      TimeDuration td = TimeCategory.minus( changeDate, dateSinceLastChange )
      def stayed = "(stayed ${td.getSeconds()}.${td.getMillis()} s)"
      println "browser changing page from '$oldPage' ${stayed} to '$newPage'"
      dateSinceLastChange = changeDate
      
      browser.report newPage.toString()
      
      // Automatic messages detection
      // Does not work for SECO
      // if an error message is displayed, then log it
      def errorDiv = browser.$('ul li[class="msg E"]')
      if (errorDiv?.value() != null) {
        errorDiv*.text().each {
          println "Page $newPage contains an error message: '${it}'"
        }
      }
      // if a warning message is displayed, then log it
      // Does not work for SECO
      def warningDiv = browser.$('ul li[class="msg W"]')
      if (!warningDiv?.value() != null) {
        warningDiv*.text().each {
          println "Page $newPage contains a warning message: '${it}'"
        }
      }
    }

  }
}
