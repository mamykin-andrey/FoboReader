package ru.mamykin.foboreader.settings.all_settings

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mamykin.foboreader.core.presentation.Actor
import ru.mamykin.foboreader.core.presentation.ComposeFeature
import ru.mamykin.foboreader.core.presentation.Reducer
import ru.mamykin.foboreader.settings.SettingsScope
import javax.inject.Inject

@SettingsScope
internal class SettingsFeature @Inject constructor(
    reducer: SettingsReducer,
    actor: SettingsActor,
    scope: CoroutineScope,
) : ComposeFeature<SettingsFeature.State, SettingsFeature.Intent, SettingsFeature.Effect, SettingsFeature.Action>(
    State.Loading,
    actor,
    reducer,
    scope,
) {
    init {
        sendIntent(Intent.LoadSettings)
    }

    // TODO: Refactor to use a single UseCase
    internal class SettingsActor @Inject constructor(
        private val getSettings: GetSettings,
        private val setBrightness: SetBrightness,
        private val setTextSize: SetTextSize,
        private val setNightTheme: SetNightTheme,
        private val setUseVibration: SetUseVibration,
        private val setTranslationColor: SetTranslationColor,
        private val setBackgroundColor: SetBackgroundColor,
    ) : Actor<Intent, Action> {

        override operator fun invoke(intent: Intent): Flow<Action> = flow {
            when (intent) {
                Intent.LoadSettings -> {
                    emit(Action.SettingsLoaded(getSettings.execute()))
                }

                is Intent.ChangeBrightness -> {
                    setBrightness.execute(intent.brightness)
                    emit(Action.SettingsLoaded(getSettings.execute()))
                }

                is Intent.ChangeNightTheme -> {
                    setNightTheme.execute(intent.isEnabled)
                    emit(Action.SettingsLoaded(getSettings.execute()))
                }

                is Intent.IncreaseTextSize -> {
                    setTextSize.execute(SetTextSize.Action.Increase)
                    emit(Action.SettingsLoaded(getSettings.execute()))
                }

                is Intent.DecreaseTextSize -> {
                    setTextSize.execute(SetTextSize.Action.Decrease)
                    emit(Action.SettingsLoaded(getSettings.execute()))
                }

                is Intent.SelectTranslationColor -> {
                    emit(Action.SelectTranslationColorSelected(getSettings.execute().translationColor))
                }

                is Intent.ChangeTranslationColor -> {
                    setTranslationColor.execute(intent.colorCode)
                    emit(Action.SettingsLoaded(getSettings.execute()))
                }

                is Intent.SelectBackgroundColor -> {
                    emit(Action.SelectBackgroundColorSelected(getSettings.execute().backgroundColor))
                }

                is Intent.ChangeBackgroundColor -> {
                    setBackgroundColor.execute(intent.colorCode)
                    emit(Action.SettingsLoaded(getSettings.execute()))
                }

                is Intent.SelectAppLanguage -> {
                    emit(Action.SelectAppLanguageSelected)
                }

                is Intent.ChangeUseVibration -> {
                    setUseVibration.execute(intent.enabled)
                    emit(Action.SettingsLoaded(getSettings.execute()))
                }
            }
        }
    }

    internal class SettingsReducer @Inject constructor() :
        Reducer<State, Action, Effect> {

        override operator fun invoke(state: State, action: Action) = when (action) {
            is Action.SettingsLoaded -> {
                State.Content(action.settings) to emptySet()
            }

            is Action.SelectTranslationColorSelected -> {
                state to setOf(Effect.SelectTranslationColor(action.currentColorCode))
            }

            is Action.SelectBackgroundColorSelected -> {
                state to setOf(Effect.SelectBackgroundColor(action.currentColorCode))
            }

            is Action.SelectAppLanguageSelected -> {
                state to setOf(Effect.SelectAppLanguage)
            }
        }
    }

    sealed class Intent {
        object LoadSettings : Intent()
        class ChangeBrightness(val brightness: Int) : Intent()
        class ChangeNightTheme(val isEnabled: Boolean) : Intent()
        object IncreaseTextSize : Intent()
        object DecreaseTextSize : Intent()
        object SelectTranslationColor : Intent()
        data class ChangeTranslationColor(val colorCode: String) : Intent()
        object SelectBackgroundColor : Intent()
        data class ChangeBackgroundColor(val colorCode: String) : Intent()
        object SelectAppLanguage : Intent()
        class ChangeUseVibration(val enabled: Boolean) : Intent()
    }

    sealed class Action {
        class SettingsLoaded(val settings: AppSettings) : Action()
        data class SelectTranslationColorSelected(val currentColorCode: String?) : Action()
        data class SelectBackgroundColorSelected(val currentColorCode: String?) : Action()
        object SelectAppLanguageSelected : Action()
    }

    sealed class Effect {
        data class SelectTranslationColor(val currentColorCode: String?) : Effect()
        data class SelectBackgroundColor(val currentColorCode: String?) : Effect()
        object SelectAppLanguage : Effect()
    }

    sealed class State {

        object Loading : State()

        data class Content(
            val settings: AppSettings,
        ) : State()
    }
}