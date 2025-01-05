package ru.mamykin.foboreader.store.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.presentation.LoggingStateDelegate
import ru.mamykin.foboreader.store.categories.BookCategory
import ru.mamykin.foboreader.store.list.StoreBook
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

    fun sendIntent(intent: Intent) = viewModelScope.launch {
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
        }
    }

    private suspend fun searchInStore(searchQuery: String) {
        state = state.copy(
            searchQuery = searchQuery,
            searchState = SearchState.Loading,
        )
        searchInStoreUseCase.execute(searchQuery).fold(
            onSuccess = {
                state = state.copy(searchState = SearchState.Loaded(it.categories, it.books))
            },
            onFailure = {
                state = state.copy(searchState = SearchState.Failed)
            },
        )
    }

    sealed class Intent {
        data class Search(val searchQuery: String) : Intent()
    }

    data class State(
        val searchQuery: String,
        val searchState: SearchState,
    )

    sealed class SearchState {
        data object NotStarted : SearchState()
        data object Loading : SearchState()
        data object Failed : SearchState()
        data class Loaded(
            val categories: List<BookCategory>,
            val books: List<StoreBook>
        ) : SearchState()
    }
}