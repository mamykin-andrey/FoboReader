package ru.mamykin.foboreader.settings.custom_color

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.mamykin.foboreader.core.navigation.AppScreen
import ru.mamykin.foboreader.core.presentation.BaseViewModel
import ru.mamykin.foboreader.settings.all_settings.GetCurrentColorUseCase
import javax.inject.Inject

@HiltViewModel
internal class ChooseCustomColorViewModel @Inject constructor(
    private val getCustomColorsUseCase: GetCustomColorsUseCase,
    private val getCurrentColorUseCase: GetCurrentColorUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ChooseCustomColorViewModel.Intent, ChooseCustomColorViewModel.State, ChooseCustomColorViewModel.Effect>(
    State()
) {
    private val colorType = savedStateHandle.get<AppScreen.ChooseColor.CustomColorType>("type")!!

    override suspend fun handleIntent(intent: Intent) {
        when (intent) {
            is Intent.LoadColors -> loadColors()
            is Intent.SelectColor -> sendEffect(Effect.Dismiss(ChooseColorResult(colorType, intent.color)))
        }
    }

    private fun loadColors() {
        if (state.colors.isNotEmpty()) return

        val currentColorCode = getCurrentColorUseCase.execute(colorType)
        val colors = getCustomColorsUseCase.execute()
        val stateColors = colors.map {
            if (it.colorCode == currentColorCode) it.copy(selected = true) else it
        }
        state = state.copy(colors = stateColors, screenTitle = getScreenTitle())
    }

    private fun getScreenTitle(): String {
        return when (colorType) {
            AppScreen.ChooseColor.CustomColorType.TRANSLATION -> "Choose translation color"
            AppScreen.ChooseColor.CustomColorType.BACKGROUND -> "Choose background color"
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