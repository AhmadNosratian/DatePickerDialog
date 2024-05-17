package ir.ahmadno.datepickerdialog.utils

/**
 * Turns all English numbers to Persian numbers
 */
fun String.toPersianNumber(): String {
    var newValue = replace("1", "۱")
    newValue = newValue.replace("2", "۲")
    newValue = newValue.replace("3", "۳")
    newValue = newValue.replace("4", "۴")
    newValue = newValue.replace("5", "۵")
    newValue = newValue.replace("6", "۶")
    newValue = newValue.replace("7", "۷")
    newValue = newValue.replace("8", "۸")
    newValue = newValue.replace("9", "۹")
    newValue = newValue.replace("0", "۰")
    return newValue
}

/**
 * Turns all Persian numbers to English numbers
 */
fun String.toEnglishNumber(): String {
    var newValue = replace("۱", "1")
    newValue = newValue.replace("۲", "2")
    newValue = newValue.replace("۳", "3")
    newValue = newValue.replace("۴", "4")
    newValue = newValue.replace("۵", "5")
    newValue = newValue.replace("۶", "6")
    newValue = newValue.replace("۷", "7")
    newValue = newValue.replace("۸", "8")
    newValue = newValue.replace("۹", "9")
    newValue = newValue.replace("۰", "0")
    return newValue
}
