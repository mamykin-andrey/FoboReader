package ru.mamykin.foboreader.settings.presentation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mamykin.foboreader.core.presentation.Actor
import ru.mamykin.foboreader.core.presentation.Feature
import ru.mamykin.foboreader.core.presentation.Reducer
import ru.mamykin.foboreader.settings.domain.model.ColorItem
import ru.mamykin.foboreader.settings.domain.usecase.GetTranslationColors
import ru.mamykin.foboreader.settings.domain.usecase.SetTranslationColor
import javax.inject.Inject

internal class SelectTranslationColorFeature @Inject constructor(
    reducer: SelectTranslationColorReducer,
    actor: SelectTranslationColorActor,
) : Feature<SelectTranslationColorFeature.State, SelectTranslationColorFeature.Intent, SelectTranslationColorFeature.Effect, SelectTranslationColorFeature.Action>(
    State(),
    actor,
    reducer
) {
    init {
        sendIntent(Intent.LoadColors)
    }

    internal class SelectTranslationColorActor @Inject constructor(
        private val getTranslationColors: GetTranslationColors,
        private val setTranslationColor: SetTranslationColor,
    ) : Actor<Intent, Action> {

        override operator fun invoke(intent: Intent): Flow<Action> = flow {
            when (intent) {
                is Intent.LoadColors -> {
                    emit(Action.ColorsLoaded(getTranslationColors.execute()))
                }
                is Intent.SelectColor -> {
                    setTranslationColor.execute(intent.color)
                    emit(Action.ColorSelected)
                }
            }
        }
    }

    internal class SelectTranslationColorReducer @Inject constructor() : Reducer<State, Action, Effect> {

        override operator fun invoke(state: State, action: Action) = when (action) {
            is Action.ColorsLoaded -> {
                state.copy(colors = action.colors) to emptySet()
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

    data class State(
        val colors: List<ColorItem>? = null,
    )
}