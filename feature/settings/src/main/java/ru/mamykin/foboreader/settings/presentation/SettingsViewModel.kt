package ru.mamykin.foboreader.settings.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.presentation.BaseViewModel
import ru.mamykin.foboreader.settings.domain.usecase.GetSettings
import ru.mamykin.foboreader.settings.domain.usecase.SetBrightness
import ru.mamykin.foboreader.settings.domain.usecase.SetNightTheme
import ru.mamykin.foboreader.settings.domain.usecase.SetTextSize
import ru.mamykin.foboreader.settings.navigation.LocalSettingsNavigator

@FlowPreview
@ExperimentalCoroutinesApi
class SettingsViewModel(
    private val getSettings: GetSettings,
    private val setBrightness: SetBrightness,
    private val setTextSize: SetTextSize,
    private val setNightTheme: SetNightTheme,
    private val localNavigator: LocalSettingsNavigator
) : BaseViewModel<ViewState, Action, Event, Nothing>(
    ViewState(isLoading = true)
) {
    override fun loadData() {
        getSettings()
            .onEach { sendAction(Action.SettingsLoaded(it)) }
            .launchIn(viewModelScope)
    }

    override fun onAction(action: Action): ViewState = when (action) {
        is Action.SettingsLoaded -> state.copy(
            isLoading = false,
            settings = action.settings
        )
    }

    override fun onEvent(event: Event) {
        viewModelScope.launch {
            when (event) {
                is Event.BrightnessChanged -> setBrightness(event.brightness)
                is Event.NightThemeChanged -> setNightTheme(event.isEnabled)
                is Event.IncreaseTextSizeClicked -> setTextSize(SetTextSize.Action.Increase)
                is Event.DecreaseTextSizeClicked -> setTextSize(SetTextSize.Action.Decrease)
                is Event.SelectReadColorClicked -> localNavigator.settingsToColorPicker()
            }
        }
    }
}