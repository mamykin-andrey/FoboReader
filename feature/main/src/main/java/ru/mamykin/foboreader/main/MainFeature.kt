package ru.mamykin.foboreader.main

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mamykin.foboreader.core.presentation.Actor
import ru.mamykin.foboreader.core.presentation.ComposeFeature
import ru.mamykin.foboreader.core.presentation.Reducer
import ru.mamykin.foboreader.core.presentation.ReducerResult
import javax.inject.Inject

@MainScope
internal class MainFeature @Inject constructor(
    actor: MainActor,
    reducer: MainReducer,
) : ComposeFeature<MainFeature.State, MainFeature.Intent, MainFeature.Effect, MainFeature.Action>(
    State.Loading,
    actor,
    reducer,
) {
    init {
        sendIntent(Intent.LoadTabs)
    }

    internal class MainActor @Inject constructor() : Actor<Intent, Action> {

        override fun invoke(intent: Intent): Flow<Action> = flow {
            when (intent) {
                is Intent.LoadTabs -> {
                    emit(Action.TabsLoaded())
                }
            }
        }
    }

    internal class MainReducer @Inject constructor(
    ) : Reducer<State, Action, Effect> {

        override fun invoke(state: State, action: Action): ReducerResult<State, Effect> = when (action) {
            is Action.TabsLoaded -> State.Content() to emptySet()
        }
    }

    sealed class Intent {
        object LoadTabs : Intent()
    }

    sealed class Effect {
    }

    sealed class Action {
        class TabsLoaded(
        ) : Action()
    }

    sealed class State {
        object Loading : State()
        class Content(
        ) : State()
    }
}