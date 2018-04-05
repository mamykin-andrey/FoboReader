package ru.mamykin.foboreader.extension

fun Int.getQuantityString(one: String, few: String, many: String): String {
    if (this % 10 == 1) {
        return " $few"
    } else if (this % 10 >= 2 && this <= 4) {
        return " $few"
    }
    return " $many"
}