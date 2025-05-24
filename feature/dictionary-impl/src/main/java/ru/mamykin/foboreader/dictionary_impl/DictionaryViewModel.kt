package ru.mamykin.foboreader.dictionary_impl

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.mamykin.foboreader.core.presentation.BaseViewModel
import ru.mamykin.foboreader.dictionary_api.DictionaryRepository
import ru.mamykin.foboreader.dictionary_api.StreakManagerUseCase
import javax.inject.Inject

@HiltViewModel
internal class DictionaryViewModel @Inject constructor(
    private val dictionaryRepository: DictionaryRepository,
    private val streakManagerUseCase: StreakManagerUseCase,
) : BaseViewModel<DictionaryViewModel.Intent, DictionaryViewModel.State, Nothing>(
    State.Loading
) {
    override suspend fun handleIntent(intent: Intent) = when (intent) {
        is Intent.LoadData -> {
            val allWords = dictionaryRepository.getAllWords()
            state = State.Content(
                learnedTodayCount = 0, // TODO: Implement today's learned count
                allWordsCount = allWords.size,
                currentStreakDays = streakManagerUseCase.getCurrentStreak(),
                bestStreakDays = streakManagerUseCase.getBestStreak(),
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