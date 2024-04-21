package ru.mamykin.foboreader.settings.all_settings

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
) : ComposeFeature<SettingsFeature.State, SettingsFeature.Intent, SettingsFeature.Effect, SettingsFeature.Action>(
    State.Loading,
    actor,
    reducer
) {
    init {
        sendIntent(Intent.LoadSettings)
    }

    internal class SettingsActor @Inject constructor(
        private val getSettings: GetSettings,
        private val setBrightness: SetBrightness,
        private val setTextSize: SetTextSize,
        private val setNightTheme: SetNightTheme,
        private val setUseVibration: SetUseVibration,
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
                is Intent.SelectReadColor -> {
                    emit(Action.SelectReadColorSelected)
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
            is Action.SelectReadColorSelected -> {
                state to setOf(Effect.SelectReadColor)
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
        object SelectReadColor : Intent()
        object SelectAppLanguage : Intent()
        class ChangeUseVibration(val enabled: Boolean) : Intent()
    }

    sealed class Action {
        class SettingsLoaded(val settings: AppSettings) : Action()
        object SelectReadColorSelected : Action()
        object SelectAppLanguageSelected : Action()
    }

    sealed class Effect {
        object SelectReadColor : Effect()
        object SelectAppLanguage : Effect()
    }

    sealed class State {

        object Loading : State()

        data class Content(
            val settings: AppSettings,
        ) : State()
    }
}