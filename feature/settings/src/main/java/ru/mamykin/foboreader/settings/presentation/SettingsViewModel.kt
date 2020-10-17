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

    override suspend fun onEvent(event: Event) {
        when (event) {
            is Event.BrightnessChanged -> {
                interactor.changeBrightness(event.brightness)
            }
            is Event.NightThemeChanged -> {
                interactor.enableNightTheme(event.nightTheme)
            }
            is Event.AutoBrightnessChanged -> {
                interactor.enableAutoBrightness(event.autoBrightness)
            }
            is Event.IncreaseTextSizeClicked -> {
                interactor.increaseTextSize()
            }
            is Event.DecreaseTextSizeClicked -> {
                interactor.decreaseTextSize()
            }
            is Event.SelectReadColorClicked -> {
                localNavigator.settingsToColorPicker()
            }
        }
    }
}