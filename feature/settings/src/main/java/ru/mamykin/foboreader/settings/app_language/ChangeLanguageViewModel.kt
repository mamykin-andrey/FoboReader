package ru.mamykin.foboreader.settings.app_language

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.mamykin.foboreader.core.presentation.BaseViewModel
import javax.inject.Inject

@HiltViewModel
internal class ChangeLanguageViewModel @Inject constructor(
    private val getAppLanguagesUseCase: GetAppLanguagesUseCase,
) : BaseViewModel<ChangeLanguageViewModel.Intent, ChangeLanguageViewModel.State, ChangeLanguageViewModel.Effect>(
    State()
) {
    override suspend fun handleIntent(intent: Intent) {
        when (intent) {
            is Intent.LoadLanguages -> {
                if (state.languages.isNotEmpty()) return
                state = State(languages = getAppLanguagesUseCase.execute())
            }

            is Intent.SelectLanguage -> sendEffect(Effect.Dismiss(intent.languageCode))
            is Intent.Dismiss -> sendEffect(Effect.Dismiss(null))
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