package ru.mamykin.foboreader.settings.custom_color

import ru.mamykin.foboreader.core.data.AppSettingsRepository
import javax.inject.Inject

internal class GetCustomColors @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository
) {
    fun execute(): List<ColorItem> {
        val selectedColorCode = appSettingsRepository.getTranslationColor() // TODO
        return createColors(selectedColorCode)
    }

    private fun createColors(selectedColorCode: String): List<ColorItem> {
        return listOf(
            Pair("#ff0000", "#ffffff"),
            Pair("#0099ff", "#ffffff"),
            Pair("#0066ff", "#ffffff"),
            Pair("#ff5050", "#ffffff"),
            Pair("#ffff00", "#000000"),
            Pair("#009900", "#ffffff"),
            Pair("#336600", "#ffffff"),
            Pair("#00cc66", "#ffffff")
        ).map { (code, checkCode) -> ColorItem(code, checkCode, code == selectedColorCode) }
    }
}