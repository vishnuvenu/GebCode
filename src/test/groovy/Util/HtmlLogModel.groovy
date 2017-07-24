package Util

/**
 * Created by nlahia on 27/07/2016.
 */
class HtmlLogModel {

  static String boldText(String text) {
    "<b>" + text + "</b>"
  }

  static String italicText(String text) {
    "<i>" + text + "</i>"
  }

  static String coloredText(String text, String color) {
    "<font color=\"" + color + "\">" + text + "</font>"
  }

  static String setupSpecText(String text) {
    coloredText(boldText(italicText(text)), "blue")
  }

  static String HelperText(String text) {
    coloredText("  " + text, "blue")
  }

  static String alertText(String text) {
    coloredText(boldText(italicText(text)), "red")
  }

  static String initTestText(String text) {
    coloredText(boldText(italicText(text)), "blue")
  }

  static String cleanupText(String text) {
    coloredText(boldText(italicText(text)), "blue")
  }

}
