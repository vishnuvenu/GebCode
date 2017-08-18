package Module

import geb.Module
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebElement


/**
 * @author FOR-SEC-SWD
 */
class DropDownModule extends Module {
  def open_link
  def selected_item
  def items_base

  static content = {
    link(wait: true) { $(open_link)[0] }
    items(wait: true) { _body.find(items_base).findAll { it.displayed } }
    item(required: false) { name -> items.find { it.text().equalsIgnoreCase(name) } }
    itemByvalue(required: false) { value -> items.find { it.value().equalsIgnoreCase(value) } }
    itemContains(required: false) { name -> items.find { it.text().contains(name) } }
    allItems(wait: true) { _body.find(items_base).findAll() }
    nonVisibleItem(required: false) { name ->
      allItems.find {
        it.getAttribute("textContent").trim().equalsIgnoreCase(name)
      }
    } //don't use .text() because it returns empty string if the element is not visible
    nonVisibleItemContains(required: false) { name ->
      allItems.find {
        it.getAttribute("textContent").trim().contains(name)
      }
    } //don't use .text() because it returns empty string if the element is not visible
    nonVisibleItemIndex(required: false) { name ->
      allItems.indexOf(allItems.find {
        it.getAttribute("textContent").trim().equalsIgnoreCase(name)
      })
    }
    nonVisibleItemIndexContains(required: false) { name ->
      allItems.indexOf(allItems.find {
        it.getAttribute("textContent").trim().contains(name)
      })
    }
    selectedItem { getSelectedName() }
  }

  private String getSelectedName() {
    def itemName
    //if the current selection has its own base use it
    if (selected_item) {
      def nav = $(selected_item)
      //use value if selection base points to an input element
      if (nav.is('input')) {
        itemName = nav.value()
      }
      //use text otherwise
      else {
        itemName = nav.text()
      }
    }

    //otherwise use base for select of dropdown
    else {
      itemName = link.text()
    }

    cutOffMetaInformation(itemName)
  }

  private String cutOffMetaInformation(String str) {
    //for some dropdown lists the items can be proceeded by a number (e.g. '1.') - remove it
    str.trim()
    str = (str =~ /^\d+\./).replaceFirst('')
    str.trim()
  }

  public boolean hasItem(String name) {
    //open dropdown and see if item exists
    _click()
    def hasItem = item(name) != null
    //close dropdown to return to previous state
    _click()
    hasItem
  }

  public int getNumberOfItems() {
    //open dropdown and count items
    _click()
    def numOfItems = items.size()
    //close dropdown to return to previous state
    _click()
    numOfItems
  }

  public void selectItem(String name) {
    _click()
    //item(name) can be null in two cases:
    // 1)the element is not included in the list
    // 2)the element is included in the list but not visible
    if (item(name) == null && nonVisibleItem(name) != null) {
      def indexOfNonVisibleObj = nonVisibleItemIndex(name)
      // Create instance of Javascript executor
      JavascriptExecutor je = (JavascriptExecutor) (browser.driver)
      //Identify the WebElement which will appear after scrolling down
      WebElement element = allItems[indexOfNonVisibleObj].getElement(0)
      je.executeScript("arguments[0].scrollIntoView(true);", element)
      item(name).click()
    } else {
      item(name).click()
    }
  }

  public void selectItem(int index) {
    _click()
    items[index].click()
  }
  public void selectItemByKeyboard(String name) {
    _click()
    link << name
    link.click()
  }

  private void _click() {
    link.wait_and_click()
  }

  /**
   * - selects item by the value and not the string name.<br>
   * - @param value<br>
   */
  public void selectItemByValue(String value) {
    _click()
    //item(name) can be null in two cases:
    // 1)the element is not included in the list
    // 2)the element is included in the list but not visible
    if (itemByvalue(value) == null && nonVisibleItem(value) != null) {
      def indexOfNonVisibleObj = nonVisibleItemIndex(value)
      // Create instance of Javascript executor
      JavascriptExecutor je = (JavascriptExecutor) (browser.driver)
      //Identify the WebElement which will appear after scrolling down
      WebElement element = allItems[indexOfNonVisibleObj].getElement(0)
      je.executeScript("arguments[0].scrollIntoView(true);", element)
      itemByvalue(value).click()
    } else {
      itemByvalue(value).click()
    }
  }
  /**
   * - selects item which contains a string name.<br>
   * - @param name<br>
   */
  public void selectItemThatContains(String name) {
    _click()
    //item(name) can be null in two cases:
    // 1)the element is not included in the list
    // 2)the element is included in the list but not visible
    if (itemContains(name) == null && nonVisibleItemContains(name) != null) {
      def indexOfNonVisibleObj = nonVisibleItemIndexContains(name)
      // Create instance of Javascript executor
      JavascriptExecutor je = (JavascriptExecutor) (browser.driver)
      //Identify the WebElement which will appear after scrolling down
      WebElement element = allItems[indexOfNonVisibleObj].getElement(0)
      je.executeScript("arguments[0].scrollIntoView(true);", element)
      itemContains(name).click()
    } else {
      itemContains(name).click()
    }
  }
}
