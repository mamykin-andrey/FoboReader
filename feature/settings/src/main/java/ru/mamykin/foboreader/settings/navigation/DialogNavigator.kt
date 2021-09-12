package ru.mamykin.foboreader.settings.navigation

import androidx.lifecycle.LiveData
import ru.mamykin.foboreader.core.presentation.SingleLiveEvent

class DialogNavigator {

    private val _navigateEvent = SingleLiveEvent<Dialog>()
    val navigateData: LiveData<Dialog> = _navigateEvent

    fun showSelectLanguageDialog() {
        _navigateEvent.setValue(Dialog.SelectAppLanguage)
    }

    fun showSelectTranslationColorDialog() {
        _navigateEvent.setValue(Dialog.SelectTranslationColor)
    }

    sealed class Dialog {

        object SelectAppLanguage : Dialog()
        object SelectTranslationColor : Dialog()
    }
}