package ru.mamykin.foboreader.settings.custom_color

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.presentation.LoggingEffectChannel
import ru.mamykin.foboreader.core.presentation.LoggingStateDelegate
import javax.inject.Inject
import javax.inject.Named

@ChooseCustomColorScope
internal class ChooseCustomColorViewModel @Inject constructor(
    private val getCustomColorsUseCase: GetCustomColors,
    @Named("title") private val screenTitle: String,
) : ViewModel() {

    var state: State by LoggingStateDelegate(State(screenTitle = screenTitle))
        private set

    private val effectChannel = LoggingEffectChannel<Effect>()
    val effectFlow = effectChannel.receiveAsFlow()

    fun sendIntent(intent: Intent) = viewModelScope.launch {
        when (intent) {
            is Intent.LoadColors -> {
                val colors = getCustomColorsUseCase.execute()
                val stateColors = colors.map {
                    if (it.colorCode == intent.currentColorCode) it.copy(selected = true) else it
                }
                state = state.copy(colors = stateColors)
            }

            is Intent.SelectColor -> {
                effectChannel.send(Effect.Dismiss(intent.color))
            }
        }
    }

    sealed class Intent {
        data class LoadColors(val currentColorCode: String?) : Intent()
        class SelectColor(val color: String?) : Intent()
    }

    sealed class Effect {
        data class Dismiss(val selectedColorCode: String?) : Effect()
    }

    data class State(
        val screenTitle: String = "",
        val colors: List<ColorItem> = emptyList()
    )
}