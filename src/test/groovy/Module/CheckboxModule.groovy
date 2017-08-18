package Module

import geb.Module
import geb.module.FormElement

class CheckboxModule extends Module {
  static content = {
    input { tag() == "input" ? $() : $("input") }
    value(required:false) { input.value() }
    //easier to pass correct base to checkbox module and avoid problem when display:none
    clickableSpan(required:false) { input.parent("span.xWidget") }
    label(required: false) { $("label").text() }
  }

  public void select() {
    if (!isSelected()) _click()
  }

  public void unselect() {
    if (isSelected())_click()
  }

  public boolean isSelected() {
    value != false
  }

  public boolean isDisabled() {
    input.module(FormElement).disabled
  }

  private void _click() {
    if (clickableSpan) {
      clickableSpan.click()
    } else {
      click()
    }
  }

}