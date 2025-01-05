package ru.mamykin.foboreader.store.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.presentation.LoggingStateDelegate
import ru.mamykin.foboreader.core.presentation.StringOrResource
import ru.mamykin.foboreader.store.categories.BookCategoryUIModel
import ru.mamykin.foboreader.store.list.StoreBookUIModel
import javax.inject.Inject

@HiltViewModel
internal class StoreSearchViewModel @Inject constructor(
    private val searchInStoreUseCase: SearchInStoreUseCase,
) : ViewModel() {

    var state: State by LoggingStateDelegate(State("", SearchState.NotStarted))
        private set

    // private val effectChannel = LoggingEffectChannel<Effect>()
    // val effectFlow = effectChannel.receiveAsFlow()
    private var curSearchJob: Job? = null

    fun sendIntent(intent: Intent) {
        viewModelScope.launch {
            when (intent) {
                is Intent.Search -> {
                    curSearchJob?.cancel()
                    if (intent.searchQuery.isEmpty()) {
                        state = state.copy(
                            searchQuery = intent.searchQuery,
                            searchState = SearchState.NotStarted,
                        )
                        return@launch
                    }
                    curSearchJob = launch { searchInStore(intent.searchQuery) }
                }

                is Intent.RetrySearch -> {
                    sendIntent(Intent.Search(state.searchQuery))
                }
            }
        }
    }

    private suspend fun searchInStore(searchQuery: String) {
        state = state.copy(
            searchQuery = searchQuery,
            searchState = SearchState.Loading,
        )
        searchInStoreUseCase.execute(searchQuery).fold(
            onSuccess = {
                state = state.copy(
                    searchState = SearchState.Loaded(
                        it.categories.map(BookCategoryUIModel::fromDomainModel),
                        it.books.map(StoreBookUIModel::fromDomainModel)
                    )
                )
            },
            onFailure = {
                state = state.copy(searchState = SearchState.Failed(StringOrResource.String(it.message.orEmpty())))
            },
        )
    }

    sealed class Intent {
        data class Search(val searchQuery: String) : Intent()
        data object RetrySearch : Intent()
    }

    data class State(
        val searchQuery: String,
        val searchState: SearchState,
    )

    sealed class SearchState {
        data object NotStarted : SearchState()
        data object Loading : SearchState()
        data class Failed(val errorMessage: StringOrResource) : SearchState()
        data class Loaded(
            val categories: List<BookCategoryUIModel>,
            val books: List<StoreBookUIModel>
        ) : SearchState()
    }
}