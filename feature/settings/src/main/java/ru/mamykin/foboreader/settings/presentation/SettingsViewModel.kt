package ru.mamykin.foboreader.settings.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.presentation.BaseViewModel
import ru.mamykin.foboreader.settings.domain.usecase.*
import ru.mamykin.foboreader.settings.navigation.DialogNavigator
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    getSettings: GetSettings,
    private val setBrightness: SetBrightness,
    private val setTextSize: SetTextSize,
    private val setNightTheme: SetNightTheme,
    private val setUseVibration: SetUseVibration
) : BaseViewModel<ViewState, Action, Event, Nothing>(
    ViewState()
) {
    private val dialogNavigator = DialogNavigator()

    val navigateData = dialogNavigator.navigateData

    init {
        getSettings.execute()
            .onEach { sendAction(Action.SettingsLoaded(it)) }
            .launchIn(viewModelScope)
    }

    override fun onAction(action: Action): ViewState = when (action) {
        is Action.SettingsLoaded -> state.copy(
            settings = action.settings
        )
    }

    override fun onEvent(event: Event) {
        viewModelScope.launch {
            when (event) {
                is Event.BrightnessChanged -> setBrightness.execute(event.brightness)
                is Event.NightThemeChanged -> setNightTheme.execute(event.isEnabled)
                is Event.IncreaseTextSizeClicked -> setTextSize.execute(SetTextSize.Action.Increase)
                is Event.DecreaseTextSizeClicked -> setTextSize.execute(SetTextSize.Action.Decrease)
                is Event.SelectReadColorClicked -> dialogNavigator.showSelectTranslationColorDialog()
                is Event.SelectAppLanguage -> dialogNavigator.showSelectLanguageDialog()
                is Event.UseVibrationChanged -> setUseVibration.execute(event.enabled)
            }
        }
    }
}