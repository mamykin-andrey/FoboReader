package ru.mamykin.foboreader.learn_new_words

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.mamykin.foboreader.core.presentation.BaseViewModel
import ru.mamykin.foboreader.dictionary_api.DictionaryRepository
import ru.mamykin.foboreader.dictionary_api.StreakManagerUseCase
import javax.inject.Inject

@HiltViewModel
internal class LearnNewWordsViewModel @Inject constructor(
    private val dictionaryRepository: DictionaryRepository,
    private val streakManagerUseCase: StreakManagerUseCase,
) : BaseViewModel<LearnNewWordsViewModel.Intent, LearnNewWordsViewModel.State, Nothing>(
    State.Loading
) {
    override suspend fun handleIntent(intent: Intent) {
        when (intent) {
            is Intent.LoadData -> loadWordsToLearn()
            is Intent.RememberSwiped -> markCurrentWordAsRemembered()
            is Intent.ForgotSwiped -> markCurrentWordAsForgotten()
            is Intent.RememberClicked -> markCurrentWordAsRemembered()
            is Intent.ForgotClicked -> markCurrentWordAsForgotten()
        }
    }

    private fun markCurrentWordAsRemembered() {
        val contentState = (state as? State.Content) ?: return
        val currentWord = contentState.wordsToLearn.firstOrNull() ?: return
        val newLearnedWords = contentState.learnedWords + currentWord
        val newWordsToLearn = contentState.wordsToLearn.drop(1)

        state = contentState.copy(
            learnedWords = newLearnedWords,
            wordsToLearn = newWordsToLearn,
        )

        // Check if all words are learned and update streak
        if (newWordsToLearn.isEmpty()) {
            streakManagerUseCase.updateStreakForCompletion()
            // Update state with new streak information
            state = (state as State.Content).copy(
                currentStreak = streakManagerUseCase.getCurrentStreak(),
                bestStreak = streakManagerUseCase.getBestStreak()
            )
        }
    }

    private fun markCurrentWordAsForgotten() {
        val contentState = (state as? State.Content) ?: return
        val currentWord = contentState.wordsToLearn.firstOrNull() ?: return
        state = contentState.copy(
            wordsToLearn = contentState.wordsToLearn.drop(1) + currentWord,
        )
    }

    private suspend fun loadWordsToLearn() {
        val allWords = dictionaryRepository.getAllWords()
        state = State.Content(
            wordsToLearn = allWords.map { WordCard(it.word, it.translation) },
            currentStreak = streakManagerUseCase.getCurrentStreak(),
            bestStreak = streakManagerUseCase.getBestStreak()
        )
    }

    sealed class Intent {
        data object LoadData : Intent()
        data object RememberClicked : Intent()
        data class RememberSwiped(val word: WordCard) : Intent()
        data object ForgotClicked : Intent()
        data class ForgotSwiped(val word: WordCard) : Intent()
    }

    sealed class State {
        data object Loading : State()

        data class Content(
            val learnedWords: List<WordCard> = emptyList(),
            val wordsToLearn: List<WordCard>,
            val currentStreak: Int = 0,
            val bestStreak: Int = 0,
        ) : State()
    }
}