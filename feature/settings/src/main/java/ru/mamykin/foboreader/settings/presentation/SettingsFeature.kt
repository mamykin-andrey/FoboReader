package ru.mamykin.foboreader.settings.presentation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mamykin.foboreader.core.presentation.Actor
import ru.mamykin.foboreader.core.presentation.Feature
import ru.mamykin.foboreader.core.presentation.Reducer
import ru.mamykin.foboreader.settings.domain.model.SettingsItem
import ru.mamykin.foboreader.settings.domain.usecase.*
import javax.inject.Inject

internal class SettingsFeature @Inject constructor(
    reducer: SettingsReducer,
    actor: SettingsActor,
    private val uiEventTransformer: UiEventTransformer,
) : Feature<Settings.ViewState, Settings.Intent, Settings.Effect, Settings.Action, Nothing>(
    Settings.ViewState(),
    actor,
    reducer
) {
    init {
        sendIntent(Settings.Intent.LoadSettings)
    }

    fun sendEvent(event: Settings.Event) {
        sendIntent(uiEventTransformer.invoke(event))
    }

    internal class SettingsActor @Inject constructor(
        private val getSettings: GetSettings,
        private val setBrightness: SetBrightness,
        private val setTextSize: SetTextSize,
        private val setNightTheme: SetNightTheme,
        private val setUseVibration: SetUseVibration,
    ) : Actor<Settings.Intent, Settings.Action> {

        override operator fun invoke(intent: Settings.Intent): Flow<Settings.Action> = flow {
            when (intent) {
                Settings.Intent.LoadSettings -> {
                    emit(Settings.Action.SettingsLoaded(getSettings.execute()))
                }
                is Settings.Intent.ChangeBrightness -> {
                    setBrightness.execute(intent.brightness)
                    emit(Settings.Action.SettingsLoaded(getSettings.execute()))
                }
                is Settings.Intent.ChangeNightTheme -> {
                    setNightTheme.execute(intent.isEnabled)
                    emit(Settings.Action.SettingsLoaded(getSettings.execute()))
                }
                is Settings.Intent.IncreaseTextSize -> {
                    setTextSize.execute(SetTextSize.Action.Increase)
                    emit(Settings.Action.SettingsLoaded(getSettings.execute()))
                }
                is Settings.Intent.DecreaseTextSize -> {
                    setTextSize.execute(SetTextSize.Action.Decrease)
                    emit(Settings.Action.SettingsLoaded(getSettings.execute()))
                }
                is Settings.Intent.SelectReadColor -> {
                    emit(Settings.Action.SelectReadColorSelected)
                }
                is Settings.Intent.SelectAppLanguage -> {
                    emit(Settings.Action.SelectAppLanguageSelected)
                }
                is Settings.Intent.ChangeUseVibration -> {
                    setUseVibration.execute(intent.enabled)
                    emit(Settings.Action.SettingsLoaded(getSettings.execute()))
                }
            }
        }
    }

    internal class SettingsReducer @Inject constructor() :
        Reducer<Settings.ViewState, Settings.Action, Settings.Effect> {

        override operator fun invoke(state: Settings.ViewState, action: Settings.Action) = when (action) {
            is Settings.Action.SettingsLoaded -> {
                state.copy(settings = action.settings) to emptySet()
            }
            is Settings.Action.SelectReadColorSelected -> {
                state to setOf(Settings.Effect.SelectReadColor)
            }
            is Settings.Action.SelectAppLanguageSelected -> {
                state to setOf(Settings.Effect.SelectAppLanguage)
            }
        }
    }

    internal class UiEventTransformer @Inject constructor() {

        operator fun invoke(event: Settings.Event): Settings.Intent = when (event) {
            is Settings.Event.DialogDismissed -> Settings.Intent.LoadSettings
            is Settings.Event.BrightnessChanged -> Settings.Intent.ChangeBrightness(event.brightness)
            is Settings.Event.NightThemeChanged -> Settings.Intent.ChangeNightTheme(event.isEnabled)
            is Settings.Event.IncreaseTextSizeClicked -> Settings.Intent.IncreaseTextSize
            is Settings.Event.DecreaseTextSizeClicked -> Settings.Intent.DecreaseTextSize
            is Settings.Event.SelectReadColorClicked -> Settings.Intent.SelectReadColor
            is Settings.Event.SelectAppLanguage -> Settings.Intent.SelectAppLanguage
            is Settings.Event.UseVibrationChanged -> Settings.Intent.ChangeUseVibration(event.enabled)
        }
    }
}


object Settings {

    sealed class Event {
        object DialogDismissed : Event()
        class BrightnessChanged(val brightness: Int) : Event()
        class NightThemeChanged(val isEnabled: Boolean) : Event()
        object IncreaseTextSizeClicked : Event()
        object DecreaseTextSizeClicked : Event()
        object SelectReadColorClicked : Event()
        object SelectAppLanguage : Event()
        class UseVibrationChanged(val enabled: Boolean) : Event()
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
        class SettingsLoaded(val settings: List<SettingsItem>) : Action()
        object SelectReadColorSelected : Action()
        object SelectAppLanguageSelected : Action()
    }

    sealed class Effect {
        object SelectReadColor : Effect()
        object SelectAppLanguage : Effect()
    }

    data class ViewState(
        val settings: List<SettingsItem>? = null
    )
}