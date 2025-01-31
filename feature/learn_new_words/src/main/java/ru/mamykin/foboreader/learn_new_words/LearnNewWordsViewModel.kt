package ru.mamykin.foboreader.learn_new_words

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.mamykin.foboreader.core.presentation.BaseViewModel
import javax.inject.Inject

@HiltViewModel
internal class LearnNewWordsViewModel @Inject constructor(
) : BaseViewModel<LearnNewWordsViewModel.Intent, LearnNewWordsViewModel.State, Nothing>(
    State.Loading
) {
    override suspend fun handleIntent(intent: Intent) {
        when (intent) {
            is Intent.LoadData -> {
                state = State.Content(
                    listOf(
                        WordCard("Hello", "Bonjour"), WordCard("Goodbye", "Au revoir"), WordCard("Thank you", "Merci")
                    )
                )
                // state = State.Content(
                //     learnedTodayCount = 5,
                //     allWordsCount = 55,
                //     currentStreakDays = 5,
                //     bestStreakDays = 132,
                // )
            }

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