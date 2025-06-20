package ru.mamykin.foboreader.learn_new_words

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.mamykin.foboreader.core.presentation.BaseViewModel
import ru.mamykin.foboreader.dictionary_api.GetWordsToLearnUseCase
import ru.mamykin.foboreader.dictionary_api.UpdateStreakUseCase
import javax.inject.Inject

@HiltViewModel
internal class LearnNewWordsViewModel @Inject constructor(
    private val updateStreakUseCase: UpdateStreakUseCase,
    private val getWordsToLearnUseCase: GetWordsToLearnUseCase,
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
            updateStreakUseCase.executeAsync(viewModelScope)
        }
    }

    private fun markCurrentWordAsForgotten() {
        val contentState = (state as? State.Content) ?: return
        val currentWord = contentState.wordsToLearn.firstOrNull() ?: return
        val wordToRepeat = currentWord.copy(word = currentWord.word, shownCount = currentWord.shownCount + 1)
        state = contentState.copy(
            wordsToLearn = contentState.wordsToLearn.drop(1) + wordToRepeat,
        )
    }

    private suspend fun loadWordsToLearn() {
        val allWords = getWordsToLearnUseCase.execute()
        state = State.Content(wordsToLearn = allWords.map { WordCard(it.word, it.translation) })
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
        ) : State()
    }
}