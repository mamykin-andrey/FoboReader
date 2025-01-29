package ru.mamykin.foboreader.dictionary

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.mamykin.foboreader.core.presentation.BaseViewModel
import ru.mamykin.foboreader.core.presentation.LoggingEffectChannel
import ru.mamykin.foboreader.core.presentation.LoggingStateDelegate
import javax.inject.Inject

@HiltViewModel
internal class DictionaryViewModel @Inject constructor(
) : BaseViewModel<DictionaryViewModel.Intent>() {

    var state: State by LoggingStateDelegate(State.Loading)
        private set

    private val effectChannel = LoggingEffectChannel<Effect>()
    val effectFlow = effectChannel.receiveAsFlow()

    override suspend fun handleIntent(intent: Intent) = when (intent) {
        is Intent.LoadData -> {
            state = State.Content(
                learnedTodayCount = 5,
                allWordsCount = 55,
                currentStreakDays = 5,
                bestStreakDays = 132,
            )
        }
    }

    sealed class Intent {
        data object LoadData : Intent()
    }

    sealed class Effect {
        class ShowSnackbar(val message: String) : Effect()
    }

    sealed class State {
        data object Loading : State()

        data class Content(
            val learnedTodayCount: Int,
            val allWordsCount: Int,
            val currentStreakDays: Int,
            val bestStreakDays: Int,
        ) : State()
    }
}