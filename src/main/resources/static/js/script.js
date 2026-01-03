/* -- */
const BUTTON_CLEAR = "buttonClear";
/* -- */

function customTableData() {
  // Format
  const tdElement = document.querySelectorAll("th, td.formEnd");
  if (tdElement != null && tdElement.length > 0) {
    tdElement.forEach(function (e) {
      e.setAttribute("colspan", "2");
    });
  }
}

function resetElement(elementID) {
  const element = document.getElementById(elementID);
  if (element != null) element.value = "";
}

function resetForm() {
  resetElement("fileName");
  resetElement("file");
  resetElement("apiKey");
}

window.addEventListener("DOMContentLoaded", () => {
  customTableData();

  const buttonClear = document.getElementById(BUTTON_CLEAR);
  if (buttonClear != null) {
    buttonClear.addEventListener("click", () => {
      resetForm();
    });
  }
});
