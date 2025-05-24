package ru.mamykin.foboreader.settings.custom_color

import ru.mamykin.foboreader.core.navigation.AppScreen
import javax.inject.Inject

internal class GetCustomColorsUseCase @Inject constructor() {

    fun execute(colorType: AppScreen.ChooseColor.CustomColorType): List<ColorItem> {
        return createColors(colorType)
    }

    private fun createColors(colorType: AppScreen.ChooseColor.CustomColorType): List<ColorItem> {
        val colors = when (colorType) {
            AppScreen.ChooseColor.CustomColorType.BACKGROUND ->
                listOf(
                    Pair("#ffffff", "#000000"),
                    Pair("#DDDDDD", "#000000"),
                    Pair("#BBBBBB", "#000000"),
                    Pair("#AAAAAA", "#000000"),
                    Pair("#000000", "#ffffff"),
                    Pair("#111111", "#ffffff"),
                    Pair("#222222", "#ffffff"),
                    Pair("#333333", "#ffffff"),
                )

            AppScreen.ChooseColor.CustomColorType.TEXT ->
                listOf(
                    Pair("#ffffff", "#000000"),
                    Pair("#DDDDDD", "#000000"),
                    Pair("#BBBBBB", "#000000"),
                    Pair("#AAAAAA", "#000000"),
                    Pair("#000000", "#ffffff"),
                    Pair("#111111", "#ffffff"),
                    Pair("#222222", "#ffffff"),
                    Pair("#333333", "#ffffff"),
                )

            AppScreen.ChooseColor.CustomColorType.TRANSLATION -> listOf(
                Pair("#ff0000", "#ffffff"),
                Pair("#0099ff", "#ffffff"),
                Pair("#0066ff", "#ffffff"),
                Pair("#ff5050", "#ffffff"),
                Pair("#ffff00", "#000000"),
                Pair("#009900", "#ffffff"),
                Pair("#336600", "#ffffff"),
                Pair("#00cc66", "#ffffff"),
            )
        }
        return colors.map { (code, checkCode) -> ColorItem(code, checkCode, false) }
    }
}