package ru.mamykin.foboreader.settings.all_settings

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.mamykin.foboreader.core.navigation.AppScreen
import ru.mamykin.foboreader.core.presentation.BaseViewModel
import ru.mamykin.foboreader.settings.app_language.SetAppLanguageUseCase
import javax.inject.Inject

@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    private val getSettings: GetSettingsUseCase,
    private val setBrightnessUseCase: SetBrightnessUseCase,
    private val setTextSizeUseCase: SetTextSizeUseCase,
    private val setNightThemeUseCase: SetNightThemeUseCase,
    private val setUseVibrationUseCase: SetUseVibrationUseCase,
    private val changeColorUseCase: ChangeColorUseCase,
    private val setAppLanguageUseCase: SetAppLanguageUseCase,
) : BaseViewModel<SettingsViewModel.Intent, SettingsViewModel.State, SettingsViewModel.Effect>(
    State.Loading
) {
    override suspend fun handleIntent(intent: Intent) {
        when (intent) {
            Intent.LoadSettings -> {
                if (state is State.Content) return
                state = State.Content(getSettings.execute())
            }

            is Intent.ChangeBrightness -> {
                setBrightnessUseCase.execute(intent.brightness)
                state = State.Content(getSettings.execute())
            }

            is Intent.SwitchTheme -> {
                setNightThemeUseCase.execute(intent.isNightTheme)
                state = State.Content(getSettings.execute())
                sendEffect(Effect.SwitchTheme(intent.isNightTheme))
            }

            is Intent.IncreaseTextSize -> {
                setTextSizeUseCase.execute(SetTextSizeUseCase.Action.Increase)
                state = State.Content(getSettings.execute())
            }

            is Intent.DecreaseTextSize -> {
                setTextSizeUseCase.execute(SetTextSizeUseCase.Action.Decrease)
                state = State.Content(getSettings.execute())
            }

            is Intent.ChooseColor -> {
                sendEffect(Effect.ChooseColor(intent.type))
            }

            is Intent.ChangeColor -> {
                val prevState = (state as? State.Content) ?: return
                val newColorCode = intent.newColorCode ?: return
                changeColorUseCase.execute(intent.type, newColorCode)
                val newSettings = getSettings.execute()
                state = prevState.copy(settings = newSettings)
            }

            is Intent.SelectAppLanguage -> {
                sendEffect(Effect.ChooseAppLanguage)
            }

            is Intent.ChangeAppLanguage -> {
                val selectedLanguageCode = intent.selectedLanguageCode ?: return
                setAppLanguageUseCase.execute(selectedLanguageCode)
                // no need to update the state since the screen will be re-created
                sendEffect(Effect.SwitchLanguage(selectedLanguageCode))
            }

            is Intent.ChangeUseVibration -> {
                setUseVibrationUseCase.execute(intent.enabled)
                state = State.Content(getSettings.execute())
            }
        }
    }

    sealed class Intent {
        data object LoadSettings : Intent()
        class ChangeBrightness(val brightness: Int) : Intent()
        class SwitchTheme(val isNightTheme: Boolean) : Intent()
        data object IncreaseTextSize : Intent()
        data object DecreaseTextSize : Intent()
        data class ChooseColor(val type: AppScreen.ChooseColor.CustomColorType) : Intent()
        data class ChangeColor(
            val type: AppScreen.ChooseColor.CustomColorType,
            val newColorCode: String?,
        ) : Intent()

        data object SelectAppLanguage : Intent()
        data class ChangeAppLanguage(val selectedLanguageCode: String?) : Intent()
        class ChangeUseVibration(val enabled: Boolean) : Intent()
    }

    sealed class State {
        data object Loading : State()

        data class Content(val settings: AppSettings) : State()
    }

    sealed class Effect {
        data class SwitchTheme(val isNightTheme: Boolean) : Effect()
        data class SwitchLanguage(val languageCode: String) : Effect()
        data class ChooseColor(val type: AppScreen.ChooseColor.CustomColorType) : Effect()
        data object ChooseAppLanguage : Effect()
    }
}