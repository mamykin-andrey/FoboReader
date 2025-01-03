package ru.mamykin.foboreader.settings.app_language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.presentation.LoggingEffectChannel
import ru.mamykin.foboreader.core.presentation.LoggingStateDelegate
import javax.inject.Inject

@HiltViewModel
internal class ChangeLanguageViewModel @Inject constructor(
    private val getAppLanguages: GetAppLanguages,
) : ViewModel() {

    var state: State by LoggingStateDelegate(State())
        private set

    private val effectChannel = LoggingEffectChannel<Effect>()
    val effectFlow = effectChannel.receiveAsFlow()
    private var isDataLoaded = false

    fun sendIntent(intent: Intent) = viewModelScope.launch {
        when (intent) {
            is Intent.LoadLanguages -> {
                if (isDataLoaded) return@launch
                state = State(languages = getAppLanguages.execute())
                isDataLoaded = true
            }

            is Intent.SelectLanguage -> {
                effectChannel.send(Effect.Dismiss(intent.languageCode))
            }

            is Intent.Dismiss -> {
                effectChannel.send(Effect.Dismiss(null))
            }
        }
    }

    sealed class Intent {
        data object LoadLanguages : Intent()
        class SelectLanguage(val languageCode: String) : Intent()
        data object Dismiss : Intent()
    }

    sealed class Effect {
        data class Dismiss(val selectedLanguageCode: String?) : Effect()
    }

    data class State(
        val languages: List<AppLanguage> = emptyList(),
    )
}