package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.settings.domain.model.ColorItem
import javax.inject.Inject

internal class GetTranslationColors @Inject constructor(
    private val appSettingsStorage: AppSettingsStorage
) {
    fun execute(): List<ColorItem> {
        val selectedColorCode = appSettingsStorage.translationColor
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