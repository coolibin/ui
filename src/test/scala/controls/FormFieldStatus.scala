package controls

case class FormFieldStatus(
  fieldName: String = "",
  fieldText: String = "",
  fieldButtonText: String = "",
  expectedStatus: WebFormControlStatus.Status = WebFormControlStatus.UNKNOWN
)
