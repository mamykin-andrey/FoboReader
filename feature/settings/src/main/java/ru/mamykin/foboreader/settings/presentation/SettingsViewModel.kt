package ru.mamykin.foboreader.settings.presentation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.presentation.BaseViewModel
import ru.mamykin.foboreader.settings.domain.interactor.SettingsInteractor
import ru.mamykin.foboreader.settings.navigation.LocalSettingsNavigator

@FlowPreview
@ExperimentalCoroutinesApi
class SettingsViewModel(
    private val interactor: SettingsInteractor,
    private val localNavigator: LocalSettingsNavigator
) : BaseViewModel<ViewState, Action, Event, Effect>(
    ViewState(isLoading = true)
) {
    override fun loadData() {
        initSettingsFlow()
        launch { interactor.loadData() }
    }

    private fun initSettingsFlow() = launch {
        interactor.settingsFlow.collect {
            sendAction(Action.SettingsLoaded(it))
        }
    }

    override fun onAction(action: Action): ViewState = when (action) {
        is Action.SettingsLoaded -> state.copy(
            isLoading = false,
            settings = action.settings
        )
    }

    override fun onEvent(event: Event) {
        when (event) {
            is Event.BrightnessChanged -> {
                launch { interactor.changeBrightness(event.brightness) }
            }
            is Event.NightThemeChanged -> {
                changeNightTheme(event)
            }
            is Event.AutoBrightnessChanged -> {
                launch { interactor.enableAutoBrightness(event.autoBrightness) }
            }
            is Event.IncreaseTextSizeClicked -> {
                launch { interactor.increaseTextSize() }
            }
            is Event.DecreaseTextSizeClicked -> {
                launch { interactor.decreaseTextSize() }
            }
            is Event.SelectReadColorClicked -> {
                launch { localNavigator.settingsToColorPicker() }
            }
        }
    }

    private fun changeNightTheme(event: Event.NightThemeChanged) = launch {
        interactor.enableNightTheme(event.isEnabled)
        sendEffect(Effect.NightThemeChanged(event.isEnabled))
    }
}