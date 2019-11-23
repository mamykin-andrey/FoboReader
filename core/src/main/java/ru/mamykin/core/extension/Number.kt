package ru.mamykin.core.extension

import java.text.DecimalFormat
import java.util.*

fun Int.getPluralsString(one: String, few: String, many: String): String {
    if (this % 10 == 1) {
        return " $few"
    } else if (this % 10 >= 2 && this <= 4) {
        return " $few"
    }
    return " $many"
}

fun Long.getBytesSizeString(): String {
    val formatter = DecimalFormat("#0.00")
    return when {
        this <= 1024 -> this.toString() + " б"
        this <= 1024 * 1024 -> formatter.format(this / 1024) + " кб"
        else -> formatter.format(this / (1024 * 1024)) + " мб"
    }
}

// TODO: refactor
fun Long.getLastModifiedString(): String {
    val daysCount = ((Date().time - this) / (1000 * 60 * 60 * 24)).toInt()
    return when {
        daysCount == 0 -> "изменён сегодня"
        daysCount < 7 -> getDaysLastOpenString(daysCount)
        daysCount < 30 -> getWeeksLastOpenString(daysCount / 7)
        daysCount < 365 -> getMonthsLastOpenString(daysCount / 30)
        else -> getYearsLastOpenString(daysCount / 365)
    }
}

private fun getDaysLastOpenString(days: Int): String {
    val plurals = days.getPluralsString("дней", "день", "дня")
    return "изменён $days $plurals назад"
}

private fun getWeeksLastOpenString(weeks: Int): String {
    val plurals = weeks.getPluralsString("недель", "неделю", "недели")
    return "изменён $weeks $plurals назад"
}

private fun getMonthsLastOpenString(months: Int): String {
    val plurals = months.getPluralsString("месяцев", "месяц", "месяца")
    return "изменён $months $plurals назад"
}

private fun getYearsLastOpenString(years: Int): String {
    val plurals = years.getPluralsString("лет", "год", "года")
    return "изменён $years $plurals назад"
}