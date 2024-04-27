package ru.mamykin.foboreader.settings.custom_color

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
) : ComposeFeature<ChooseCustomColorFeature.State, ChooseCustomColorFeature.Intent, ChooseCustomColorFeature.Effect, ChooseCustomColorFeature.Action>(
    State.Loaded(emptyList()),
    actor,
    reducer
) {
    init {
        sendIntent(Intent.LoadColors)
    }

    internal class ChooseCustomColorActor @Inject constructor(
        private val getCustomColorsUseCase: GetCustomColors,
        private val setTranslationColor: SetTranslationColor, // TODO
    ) : Actor<Intent, Action> {

        override operator fun invoke(intent: Intent): Flow<Action> = flow {
            when (intent) {
                is Intent.LoadColors -> {
                    emit(Action.ColorsLoaded(getCustomColorsUseCase.execute()))
                }
                is Intent.SelectColor -> {
                    setTranslationColor.execute(intent.color)
                    emit(Action.ColorSelected)
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
                state to setOf(Effect.Dismiss)
            }
        }
    }

    sealed class Intent {
        object LoadColors : Intent()
        class SelectColor(val color: String) : Intent()
    }

    sealed class Action {
        class ColorsLoaded(val colors: List<ColorItem>) : Action()
        object ColorSelected : Action()
    }

    sealed class Effect {
        object Dismiss : Effect()
    }

    sealed class State {
        data class Loaded(
            val colors: List<ColorItem>
        ) : State()
    }
}