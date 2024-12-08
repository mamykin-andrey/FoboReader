package ru.mamykin.foboreader.settings.custom_color

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mamykin.foboreader.core.presentation.Actor
import ru.mamykin.foboreader.core.presentation.ComposeFeature
import ru.mamykin.foboreader.core.presentation.Reducer
import ru.mamykin.foboreader.settings.SettingsScope
import javax.inject.Inject

@SettingsScope
internal class ChooseCustomColorFeature @Inject constructor(
    reducer: ChooseCustomColorReducer,
    actor: ChooseCustomColorActor,
    scope: CoroutineScope,
) : ComposeFeature<ChooseCustomColorFeature.State, ChooseCustomColorFeature.Intent, ChooseCustomColorFeature.Effect, ChooseCustomColorFeature.Action>(
    State.Loaded(emptyList()),
    actor,
    reducer,
    scope,
) {
    internal class ChooseCustomColorActor @Inject constructor(
        private val getCustomColorsUseCase: GetCustomColors,
    ) : Actor<Intent, Action> {

        override operator fun invoke(intent: Intent): Flow<Action> = flow {
            when (intent) {
                is Intent.LoadColors -> {
                    val colors = getCustomColorsUseCase.execute()
                    val stateColors = colors.map {
                        if (it.colorCode == intent.currentColorCode) it.copy(selected = true) else it
                    }
                    emit(Action.ColorsLoaded(stateColors))
                }

                is Intent.SelectColor -> {
                    emit(Action.ColorSelected(intent.color))
                }
            }
        }
    }

    internal class ChooseCustomColorReducer @Inject constructor() : Reducer<State, Action, Effect> {

        override operator fun invoke(state: State, action: Action) = when (action) {
            is Action.ColorsLoaded -> {
                State.Loaded(colors = action.colors) to emptySet()
            }

            is Action.ColorSelected -> {
                state to setOf(Effect.Dismiss(action.colorCode))
            }
        }
    }

    sealed class Intent {
        data class LoadColors(val currentColorCode: String?) : Intent()
        class SelectColor(val color: String) : Intent()
    }

    sealed class Action {
        class ColorsLoaded(val colors: List<ColorItem>) : Action()
        data class ColorSelected(val colorCode: String) : Action()
    }

    sealed class Effect {
        data class Dismiss(val selectedColorCode: String?) : Effect()
    }

    sealed class State {
        data class Loaded(
            val colors: List<ColorItem>
        ) : State()
    }
}