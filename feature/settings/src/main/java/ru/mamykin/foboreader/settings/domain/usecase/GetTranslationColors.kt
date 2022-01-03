package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.core.domain.usecase.base.Result
import ru.mamykin.foboreader.settings.domain.model.ColorItem
import javax.inject.Inject

class GetTranslationColors @Inject constructor(
    private val appSettingsStorage: AppSettingsStorage
) {
    fun execute(): Result<List<ColorItem>> {
        return runCatching {
            Result.success(run {
                val selectedColorCode = appSettingsStorage.translationColorCodeField.get()
                createColors(selectedColorCode)
            })
        }.getOrElse { Result.error(it) }
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