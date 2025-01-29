package ru.mamykin.foboreader.learn_new_words

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.mamykin.foboreader.core.presentation.BaseViewModel
import ru.mamykin.foboreader.core.presentation.LoggingStateDelegate
import javax.inject.Inject

@HiltViewModel
internal class LearnNewWordsViewModel @Inject constructor(
) : BaseViewModel<LearnNewWordsViewModel.Intent>() {

    var state: State by LoggingStateDelegate(State.Content)
        private set

    override suspend fun handleIntent(intent: Intent) = when (intent) {
        is Intent.LoadData -> {
            // state = State.Content(
            //     learnedTodayCount = 5,
            //     allWordsCount = 55,
            //     currentStreakDays = 5,
            //     bestStreakDays = 132,
            // )
        }
    }

    sealed class Intent {
        data object LoadData : Intent()
    }

    sealed class State {
        data object Loading : State()

        data object Content : State()
    }
}