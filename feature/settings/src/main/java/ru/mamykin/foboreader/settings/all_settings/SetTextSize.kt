package ru.mamykin.foboreader.settings.all_settings

import ru.mamykin.foboreader.core.data.AppSettingsRepository
import javax.inject.Inject

internal class SetTextSize @Inject constructor(
    private val appSettings: AppSettingsRepository
) {
    companion object {
        private const val MIN_READ_TEXT_SIZE = 10
        private const val MAX_READ_TEXT_SIZE = 30
    }

    fun execute(action: Action) {
        val textSize = appSettings.getReadTextSize()
        val newSize = when (action) {
            is Action.Increase -> textSize + 1
            is Action.Decrease -> textSize - 1
        }
        if (isSizeValid(newSize)) {
            appSettings.setReadTextSize(newSize)
        }
    }

    private fun isSizeValid(size: Int): Boolean {
        return size in MIN_READ_TEXT_SIZE..MAX_READ_TEXT_SIZE
    }

    sealed class Action {
        object Increase : Action()
        object Decrease : Action()
    }
}