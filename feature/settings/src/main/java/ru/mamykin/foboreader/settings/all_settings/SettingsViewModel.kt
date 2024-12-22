package ru.mamykin.foboreader.settings.all_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.presentation.LoggingEffectChannel
import ru.mamykin.foboreader.core.presentation.LoggingStateDelegate
import ru.mamykin.foboreader.settings.SettingsScope
import javax.inject.Inject

// TODO: Refactor to use a single UseCase
@SettingsScope
internal class SettingsViewModel @Inject constructor(
    private val getSettings: GetSettings,
    private val setBrightness: SetBrightness,
    private val setTextSize: SetTextSize,
    private val setNightTheme: SetNightTheme,
    private val setUseVibration: SetUseVibration,
    private val setTranslationColor: SetTranslationColor,
    private val setBackgroundColor: SetBackgroundColor,
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

            is Intent.ChangeNightTheme -> {
                setNightTheme.execute(intent.isEnabled)
                state = State.Content(getSettings.execute())
            }

            is Intent.IncreaseTextSize -> {
                setTextSize.execute(SetTextSize.Action.Increase)
                state = State.Content(getSettings.execute())
            }

            is Intent.DecreaseTextSize -> {
                setTextSize.execute(SetTextSize.Action.Decrease)
                state = State.Content(getSettings.execute())
            }

            is Intent.SelectTranslationColor -> {
                val prevState = state as? State.Content ?: return@launch
                state = prevState.copy(translationColorDialogCode = getSettings.execute().translationColor)
            }

            is Intent.ChangeTranslationColor -> {
                val prevState = (state as? State.Content) ?: return@launch
                var newSettings = prevState.settings
                if (intent.colorCode != null) {
                    setTranslationColor.execute(intent.colorCode)
                    newSettings = getSettings.execute()
                }
                state = prevState.copy(
                    settings = newSettings,
                    translationColorDialogCode = null,
                )
            }

            is Intent.SelectBackgroundColor -> {
                val prevState = state as? State.Content ?: return@launch
                state = prevState.copy(backgroundColorDialogCode = getSettings.execute().backgroundColor)
            }

            is Intent.ChangeBackgroundColor -> {
                val prevState = (state as? State.Content) ?: return@launch
                var newSettings = prevState.settings
                if (intent.colorCode != null) {
                    setBackgroundColor.execute(intent.colorCode)
                    newSettings = getSettings.execute()
                }
                state = prevState.copy(
                    settings = newSettings,
                    backgroundColorDialogCode = null,
                )
            }

            is Intent.SelectAppLanguage -> {
                effectChannel.send(Effect.SelectAppLanguage)
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
        class ChangeNightTheme(val isEnabled: Boolean) : Intent()
        data object IncreaseTextSize : Intent()
        data object DecreaseTextSize : Intent()
        data object SelectTranslationColor : Intent()
        data class ChangeTranslationColor(val colorCode: String?) : Intent()
        data object SelectBackgroundColor : Intent()
        data class ChangeBackgroundColor(val colorCode: String?) : Intent()
        data object SelectAppLanguage : Intent()
        class ChangeUseVibration(val enabled: Boolean) : Intent()
    }

    sealed class Effect {
        data object SelectAppLanguage : Effect()
    }

    sealed class State {
        data object Loading : State()

        data class Content(
            val settings: AppSettings,
            val backgroundColorDialogCode: String? = null,
            val translationColorDialogCode: String? = null,
        ) : State()
    }
}