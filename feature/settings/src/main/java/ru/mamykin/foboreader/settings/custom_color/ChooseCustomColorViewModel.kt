package ru.mamykin.foboreader.settings.custom_color

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.presentation.LoggingEffectChannel
import ru.mamykin.foboreader.core.presentation.LoggingStateDelegate
import ru.mamykin.foboreader.settings.all_settings.GetCurrentColorUseCase
import ru.mamykin.foboreader.settings.common.CustomColorType
import javax.inject.Inject

@HiltViewModel
internal class ChooseCustomColorViewModel @Inject constructor(
    private val getCustomColorsUseCase: GetCustomColorsUseCase,
    getCurrentColorUseCase: GetCurrentColorUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val colorType: CustomColorType = savedStateHandle.get<CustomColorType>("type")!!
    private val colorCode: String = getCurrentColorUseCase.execute(colorType)
    private val screenTitle: String = when (colorType) {
        CustomColorType.TRANSLATION -> "Choose translation color"
        CustomColorType.BACKGROUND -> "Choose background color"
    }

    var state: State by LoggingStateDelegate(State(screenTitle = screenTitle))
        private set

    private val effectChannel = LoggingEffectChannel<Effect>()
    val effectFlow = effectChannel.receiveAsFlow()
    private var isDataLoaded = false

    fun sendIntent(intent: Intent) = viewModelScope.launch {
        when (intent) {
            is Intent.LoadColors -> {
                if (isDataLoaded) return@launch
                val colors = getCustomColorsUseCase.execute()
                val stateColors = colors.map {
                    if (it.colorCode == colorCode) it.copy(selected = true) else it
                }
                state = state.copy(colors = stateColors)
                isDataLoaded = true
            }

            is Intent.SelectColor -> {
                effectChannel.send(Effect.Dismiss(ChooseColorResult(colorType, intent.color)))
            }
        }
    }

    sealed class Intent {
        data object LoadColors : Intent()
        class SelectColor(val color: String?) : Intent()
    }

    sealed class Effect {
        data class Dismiss(val result: ChooseColorResult) : Effect()
    }

    data class State(
        val screenTitle: String = "",
        val colors: List<ColorItem> = emptyList()
    )
}