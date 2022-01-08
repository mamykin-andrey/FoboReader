package ru.mamykin.foboreader.settings.presentation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mamykin.foboreader.core.presentation.Actor
import ru.mamykin.foboreader.core.presentation.Feature
import ru.mamykin.foboreader.core.presentation.Reducer
import ru.mamykin.foboreader.settings.di.SettingsScope
import ru.mamykin.foboreader.settings.domain.model.AppLanguage
import ru.mamykin.foboreader.settings.domain.usecase.GetAppLanguages
import ru.mamykin.foboreader.settings.domain.usecase.SetAppLanguage
import javax.inject.Inject

@SettingsScope
internal class ChangeLanguageFeature @Inject constructor(
    reducer: ChangeLanguageReducer,
    actor: ChangeLanguageActor,
) : Feature<ChangeLanguageFeature.State, ChangeLanguageFeature.Intent, ChangeLanguageFeature.Effect, ChangeLanguageFeature.Action>(
    State(),
    actor,
    reducer
) {
    init {
        sendIntent(Intent.LoadLanguages)
    }

    internal class ChangeLanguageActor @Inject constructor(
        private val getAppLanguages: GetAppLanguages,
        private val setAppLanguage: SetAppLanguage,
    ) : Actor<Intent, Action> {

        override operator fun invoke(intent: Intent): Flow<Action> = flow {
            when (intent) {
                is Intent.LoadLanguages -> {
                    emit(Action.LanguagesLoaded(getAppLanguages.execute()))
                }
                is Intent.SelectLanguage -> {
                    setAppLanguage.execute(intent.languageCode)
                    emit(Action.LanguageSelected)
                }
            }
        }
    }

    internal class ChangeLanguageReducer @Inject constructor() : Reducer<State, Action, Effect> {

        override operator fun invoke(state: State, action: Action) = when (action) {
            is Action.LanguagesLoaded -> {
                state.copy(languages = action.languages) to emptySet()
            }
            is Action.LanguageSelected -> {
                state to setOf(Effect.Dismiss)
            }
        }
    }

    sealed class Intent {
        object LoadLanguages : Intent()
        class SelectLanguage(val languageCode: String) : Intent()
    }

    sealed class Action {
        class LanguagesLoaded(val languages: List<AppLanguage>) : Action()
        object LanguageSelected : Action()
    }

    sealed class Effect {
        object Dismiss : Effect()
    }

    data class State(
        val languages: List<AppLanguage>? = null
    )
}