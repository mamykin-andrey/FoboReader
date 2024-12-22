package ru.mamykin.foboreader.settings.app_language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.presentation.LoggingEffectChannel
import ru.mamykin.foboreader.core.presentation.LoggingStateDelegate
import javax.inject.Inject

@ChangeLanguageScope
internal class ChangeLanguageViewModel @Inject constructor(
    private val getAppLanguages: GetAppLanguages,
    private val setAppLanguage: SetAppLanguage,
) : ViewModel() {

    var state: State by LoggingStateDelegate(State())
        private set

    private val effectChannel = LoggingEffectChannel<Effect>()
    val effectFlow = effectChannel.receiveAsFlow()

    fun sendIntent(intent: Intent) = viewModelScope.launch {
        when (intent) {
            is Intent.LoadLanguages -> {
                state = State(languages = getAppLanguages.execute())
            }

            is Intent.SelectLanguage -> {
                setAppLanguage.execute(intent.languageCode)
                effectChannel.send(Effect.Dismiss)
            }

            is Intent.Dismiss -> {
                effectChannel.send(Effect.Dismiss)
            }
        }
    }

    sealed class Intent {
        data object LoadLanguages : Intent()
        class SelectLanguage(val languageCode: String) : Intent()
        data object Dismiss : Intent()
    }

    sealed class Effect {
        data object Dismiss : Effect()
    }

    data class State(
        val languages: List<AppLanguage> = emptyList(),
    )
}