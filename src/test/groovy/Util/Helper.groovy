package Util

import geb.navigator.Navigator

import java.text.Normalizer

/**
 * Created by philt on 09/10/2014.
 */
class Helper {
  static final MANDATORY_COLOR = "rgba(255, 255, 204, 1)"
  static final HIGHLIGHT_COLOR = "rgba(255, 0, 0, 1)"

  public static String randomDate(int numOfDays, String format = "ddMMM") {
    (new Date() + new Random().nextInt(20) + numOfDays).format(format)
  }

  public static String journeyDate(int numOfDays, String format = "ddMMM") {
    (new Date() + numOfDays).format(format)
  }
  
  /**
   * Get date in a specific format. By default standard SECO format is applied. <br>
   * @param date  The original date <br>
   * @param format The desired format. By default most frequent SECO format 'dMMMMyyyy' <br>
   * @paramt inUpperCase By default true to produce most frequent SECO format in upper case <br>
   * @return The formatted date <br>
   */
  public static String formattedDate(String date, String format = "dMMMMyyyy", boolean upperCase = true) {
    if (upperCase) {
      new Date(date).format(format).toUpperCase()
    } else {
      new Date(date).format(format)
    }
  }

  public static String nextYear(String format = "yy") {
    (new Date() + 366).format(format)
  }

  public static String getYear(int numOfYear, String format = "yy") {
    (new Date() + (366 * numOfYear)).format(format)
  }

  /**
   * Encrypt credit card number: last 4 numbers are shown, the others are replaced by *.<br>
   *
   * @param ccNumber : credit card number<br>
   * @return String the encrypted credit card number
   */
  public static String encryptedCC(String ccNumber) {
    '*' * (ccNumber.length() - 4) + ccNumber[-4..-1]
  }

  public static int getCurrentDay() {
    (new Date().day)
  }

  public static String getSameDayOfTheWeek(int dayOfTheWeek, int delay = 0) {
    journeyDate(((dayOfTheWeek - getCurrentDay()) % 7 + 7) % 7 + delay)
  }

  public static boolean isRecordLocator(resaNumber) {
    resaNumber ==~ /[A-Z0-9]{6}/
  }

  public static boolean isReservationNumber(resaNumber) {
    resaNumber ==~ /\d{4}-\d{4}/
  }

  /**
   * Check if a field is mandatory.
   * @param nav The field
   * @param jspField to know if the field is built with Aria JSP or not
   * @return true if the field is mandatory, false otherwise
   */
//  public static boolean checkMandatoryField(Navigator nav, boolean jspField = false) {
//    def parent
//    if (nav instanceof DropDownModule) {
//      parent = nav.link.parent()
//    } else {
//      parent = nav.parent()
//    }
//    assert parent.classes().find {
//      it.contains("mandatory")
//    }
//    if (!jspField) {
//      assert parent.firstElement().getCssValue("background-color") == MANDATORY_COLOR
//    } else {
//      assert nav.firstElement().getCssValue("background-color") == MANDATORY_COLOR
//    }
//    true
//  }

  /**
   * Check if the field is highlighted.
   * @param nav the field
   * @param jspField to know if the field is built with Aria JSP or not
   * @return true if the field is highlighted, false otherwise
   */
  public static boolean checkHighlightedField(Navigator nav, boolean jspField = false) {
    def parent = nav.parent()
    assert parent.classes().find {
      it.toLowerCase().contains("error")
    }
    def elt = (jspField) ? nav.firstElement() : parent.firstElement()
    assert elt.getCssValue("border-top-color") == HIGHLIGHT_COLOR
    assert elt.getCssValue("border-right-color") == HIGHLIGHT_COLOR
    assert elt.getCssValue("border-bottom-color") == HIGHLIGHT_COLOR
    assert elt.getCssValue("border-bottom-color") == HIGHLIGHT_COLOR
    true
  }

  /**
   * Remove accent on a string
   */
  public static String removeAccentOnString(String txt) {
    Normalizer.normalize(txt, Normalizer.Form.NFD).replaceAll(/\p{InCombiningDiacriticalMarks}+/, '')
  }

  /**
   * Common method to return random string of given length.default size =5 <br>
   * @param charlength<br>
   * @return String <br>
   */
//  public static String randomString(int charlength = 5) {
//    RandomStringUtils.randomNumeric(charlength)
//  }

  /**
   * Gets key using a value from the map.<br>
   * @param mapName : data model map <br>
   * @param value <br>
   * @return : key <br>
   */
  public static Object getKeyFromValue(Map mapName, Object value) {
    for (Object object : mapName.keySet()) {
      if (mapName.get(object) == (value)) {
        return object
      }
    }
  }
}
