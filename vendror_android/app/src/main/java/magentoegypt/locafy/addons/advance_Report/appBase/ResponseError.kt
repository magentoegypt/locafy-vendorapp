package magentoegypt.locafy.addons.advance_Report.appBase

object ResponseError {
    const val EMPTY_BODY = "No Data found, try again !"
    const val FAILED_TO_LOAD = "Failed to load Data, retry !"
    const val ERROR_404 = "Error Code 404 !!"
    const val ERROR_400 = "Incorrect Syntax"
    const val ERROR_401 = "un-authorized request"
    const val ERROR_403 = "un-authorized, permission required"
    const val ERROR_408 = "request timeout"
    const val ERROR_415 = "un-supported media type"
    const val ERROR_426 = "upgrade required"
    const val ERROR_500 = "internal server error"
    const val ERROR_501 = "not implemented,Http method is not supported"
    const val ERROR_503 = "service un available"
    const val ERROR_505 = "Http version not supported"
}
