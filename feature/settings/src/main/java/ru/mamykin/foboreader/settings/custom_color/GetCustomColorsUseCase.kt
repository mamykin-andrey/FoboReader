package ru.mamykin.foboreader.settings.custom_color

import javax.inject.Inject

internal class GetCustomColorsUseCase @Inject constructor() {

    fun execute(): List<ColorItem> {
        return createColors()
    }

    private fun createColors(): List<ColorItem> {
        return listOf(
            Pair("#ff0000", "#ffffff"),
            Pair("#0099ff", "#ffffff"),
            Pair("#0066ff", "#ffffff"),
            Pair("#ff5050", "#ffffff"),
            Pair("#ffff00", "#000000"),
            Pair("#009900", "#ffffff"),
            Pair("#336600", "#ffffff"),
            Pair("#00cc66", "#ffffff")
        ).map { (code, checkCode) -> ColorItem(code, checkCode, false) }
    }
}