package ru.mamykin.foboreader.learn_new_words

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.mamykin.foboreader.core.presentation.BaseViewModel
import javax.inject.Inject

@HiltViewModel
internal class LearnNewWordsViewModel @Inject constructor(
) : BaseViewModel<LearnNewWordsViewModel.Intent, LearnNewWordsViewModel.State, Nothing>(
    State.Loading
) {
    override suspend fun handleIntent(intent: Intent) = when (intent) {
        is Intent.LoadData -> {
            state = State.Content
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