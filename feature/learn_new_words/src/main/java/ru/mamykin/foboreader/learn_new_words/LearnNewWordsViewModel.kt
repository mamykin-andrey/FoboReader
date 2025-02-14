package ru.mamykin.foboreader.learn_new_words

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.mamykin.foboreader.core.presentation.BaseViewModel
import ru.mamykin.foboreader.dictionary_api.DictionaryRepository
import javax.inject.Inject

@HiltViewModel
internal class LearnNewWordsViewModel @Inject constructor(
    private val dictionaryRepository: DictionaryRepository,
) : BaseViewModel<LearnNewWordsViewModel.Intent, LearnNewWordsViewModel.State, Nothing>(
    State.Loading
) {
    override suspend fun handleIntent(intent: Intent) {
        when (intent) {
            is Intent.LoadData -> loadWordsToLearn()
            is Intent.RememberSwiped -> {
                val contentState = (state as? State.Content) ?: return
                state = contentState.copy(
                    learnedWords = contentState.learnedWords + 1
                )
            }

            is Intent.ForgotSwiped -> {
            }

            is Intent.RememberClicked -> {
            }

            is Intent.ForgotClicked -> {
            }
        }
    }

    private suspend fun loadWordsToLearn() {
        val allWords = dictionaryRepository.getAllWords()
        state = State.Content(allWords.map { WordCard(it.word, it.translation) })
    }

    sealed class Intent {
        data object LoadData : Intent()
        data class RememberClicked(val word: WordCard) : Intent()
        data class RememberSwiped(val word: WordCard) : Intent()
        data class ForgotClicked(val word: WordCard) : Intent()
        data class ForgotSwiped(val word: WordCard) : Intent()
    }

    sealed class State {
        data object Loading : State()

        data class Content(
            val words: List<WordCard>,
            val learnedWords: Int = 0,
        ) : State()
    }
}