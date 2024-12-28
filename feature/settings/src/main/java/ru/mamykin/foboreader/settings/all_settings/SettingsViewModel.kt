package ru.mamykin.foboreader.settings.all_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.presentation.LoggingEffectChannel
import ru.mamykin.foboreader.core.presentation.LoggingStateDelegate
import ru.mamykin.foboreader.settings.app_language.SetAppLanguage
import ru.mamykin.foboreader.settings.common.CustomColorType
import javax.inject.Inject

// TODO: Refactor to use a single UseCase
@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    private val getSettings: GetSettings,
    private val setBrightness: SetBrightness,
    private val setTextSize: SetTextSize,
    private val setNightTheme: SetNightTheme,
    private val setUseVibration: SetUseVibration,
    private val changeColorUseCase: ChangeColorUseCase,
    private val setAppLanguage: SetAppLanguage,
) : ViewModel() {

    var state: State by LoggingStateDelegate(State.Loading)
        private set

    private val effectChannel = LoggingEffectChannel<Effect>()
    val effectFlow = effectChannel.receiveAsFlow()

    fun sendIntent(intent: Intent) = viewModelScope.launch {
        when (intent) {
            Intent.LoadSettings -> {
                state = State.Content(getSettings.execute())
            }

            is Intent.ChangeBrightness -> {
                setBrightness.execute(intent.brightness)
                state = State.Content(getSettings.execute())
            }

            is Intent.SwitchTheme -> {
                setNightTheme.execute(intent.isNightTheme)
                state = State.Content(getSettings.execute())
                effectChannel.send(Effect.SwitchTheme(intent.isNightTheme))
            }

            is Intent.IncreaseTextSize -> {
                setTextSize.execute(SetTextSize.Action.Increase)
                state = State.Content(getSettings.execute())
            }

            is Intent.DecreaseTextSize -> {
                setTextSize.execute(SetTextSize.Action.Decrease)
                state = State.Content(getSettings.execute())
            }

            is Intent.ChooseColor -> {
                val settings = getSettings.execute()
                val curColorCode = when (intent.type) {
                    CustomColorType.TRANSLATION -> settings.translationColor
                    CustomColorType.BACKGROUND -> settings.backgroundColor
                }
                val screenTitle = when (intent.type) {
                    CustomColorType.TRANSLATION -> "Choose translation color"
                    CustomColorType.BACKGROUND -> "Choose background color"
                }
                effectChannel.send(Effect.ChooseColor(intent.type, curColorCode, screenTitle))
            }

            is Intent.ChangeColor -> {
                val prevState = (state as? State.Content) ?: return@launch
                val newColorCode = intent.newColorCode ?: return@launch
                changeColorUseCase.execute(intent.type, newColorCode)
                val newSettings = getSettings.execute()
                state = prevState.copy(settings = newSettings)
            }

            is Intent.SelectAppLanguage -> {
                val prevState = (state as? State.Content) ?: return@launch
                state = prevState.copy(isChangeLanguageDialogShown = true)
            }

            is Intent.AppLanguageChanged -> {
                val selectedLanguageCode = intent.selectedLanguageCode ?: return@launch
                setAppLanguage.execute(selectedLanguageCode)
                val prevState = (state as? State.Content) ?: return@launch
                state = prevState.copy(isChangeLanguageDialogShown = false)
                effectChannel.send(Effect.SwitchLanguage(selectedLanguageCode))
            }

            is Intent.ChangeUseVibration -> {
                setUseVibration.execute(intent.enabled)
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
        data class ChooseColor(val type: CustomColorType) : Intent()
        data class ChangeColor(
            val type: CustomColorType,
            val newColorCode: String?,
        ) : Intent()

        data object SelectAppLanguage : Intent()
        data class AppLanguageChanged(val selectedLanguageCode: String?) : Intent()
        class ChangeUseVibration(val enabled: Boolean) : Intent()
    }

    sealed class State {
        data object Loading : State()

        data class Content(
            val settings: AppSettings,
            val isChangeLanguageDialogShown: Boolean = false,
        ) : State()
    }

    sealed class Effect {
        data class SwitchTheme(val isNightTheme: Boolean) : Effect()
        data class SwitchLanguage(val languageCode: String) : Effect()
        data class ChooseColor(
            val type: CustomColorType,
            val curColorCode: String,
            val screenTitle: String,
        ) : Effect()
    }
}