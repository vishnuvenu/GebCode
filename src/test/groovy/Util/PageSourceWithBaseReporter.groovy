package Util

import geb.Browser
import geb.report.PageSourceReporter
import Util.BaseSpec

/**
 * Writes the source content of the browser's current page as a html file.
 */
class PageSourceWithBaseReporter extends PageSourceReporter {

  @Override
  protected writePageSource(File file, Browser browser) {
    String source = getPageSource(browser)
    file.write((source =~ /<head>/).replaceFirst("<head><base href=\"${BaseSpec.get_baseUrl()}/\" >"))
  }

}