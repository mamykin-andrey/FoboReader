package ru.mamykin.foboreader.dictionary

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.mamykin.foboreader.core.presentation.BaseViewModel
import javax.inject.Inject

@HiltViewModel
internal class DictionaryViewModel @Inject constructor(
) : BaseViewModel<DictionaryViewModel.Intent, DictionaryViewModel.State, Nothing>(
    State.Loading
) {
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