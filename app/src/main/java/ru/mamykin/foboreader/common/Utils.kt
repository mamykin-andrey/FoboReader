package ru.mamykin.foboreader.common

import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    /**
     * Получаем строку с подходящей еденицей измерения
     * @param size размер файла
     * @return строка с размером файла и соотв. еденицей измерения
     */
    fun getSizeString(size: Long): String {
        val formatter = DecimalFormat("#0.00")
        return if (size <= 1024) {
            size.toString() + " б"
        } else if (size <= 1024.0 * 1024.0) {
            formatter.format(size / 1024.0) + " кб"
        } else {
            formatter.format(size / (1024.0 * 1024.0)) + " мб"
        }
    }

    /**
     * Получаем строку с датой последнего изменения файла
     * @param modifiedTime дата изменения в формате timestamp
     * @return строка с с датой последнего изменения файла
     */
    fun getLastModifiedString(modifiedTime: Long): String {
        val diffInDays = (Date().time - modifiedTime) / (1000 * 60 * 60 * 24)
        return if (diffInDays == 0L) {
            "изменён сегодня"
        } else if (diffInDays < 7) {
            // Прошло меньше одной недели
            "изменён " + diffInDays + getRightEnding(diffInDays, "дней", "день", "дня") + " назад"
        } else if (diffInDays < 30) {
            ("изменён " + diffInDays / 7
                    + getRightEnding(diffInDays / 7, "недель", "неделю", "недели") + " назад")
        } else if (diffInDays < 365) {
            ("изменён " + diffInDays / 30
                    + getRightEnding(diffInDays / 30, "месяцев", "месяц", "месяца") + " назад")
        } else {
            ("изменён " + diffInDays / 365
                    + getRightEnding(diffInDays / 365, "лет", "год", "года") + " назад")
        }
    }

    /**
     * Выбираем правильное окончание для числа
     * @param number       само число
     * @param firstFormat  первый вариант
     * @param secondFormat второй вариант
     * @param thirdFormat  третий вариант
     * @return правильное окончание для числа
     */
    fun getRightEnding(number: Long, firstFormat: String, secondFormat: String,
                       thirdFormat: String): String {
        if (number % 10 == 1L) {
            return " $secondFormat"
        } else if (number % 10 >= 2 && number <= 4) {
            return " $thirdFormat"
        }
        return " $firstFormat"
    }

    /**
     * Получаем дату из строки в нужном формате
     * @param src строка с датой
     * @return дата, полученная из строки
     */
    fun getDateFromString(src: String): Date? {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale("ru"))
        try {
            return format.parse(src)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * Получем строку с датой из объекта [Date]
     * @param date объект с датой
     * @return строка с форматированной датой
     */
    fun getFormattedDate(date: Date): String? {
        try {
            val format = SimpleDateFormat("MMM dd, yyyy", Locale("ru"))
            return format.format(date)
        } catch (e: Exception) {
            return null
        }

    }
}