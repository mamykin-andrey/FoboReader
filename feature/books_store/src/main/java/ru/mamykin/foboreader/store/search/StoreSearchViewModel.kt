package ru.mamykin.foboreader.store.search

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import ru.mamykin.foboreader.core.extension.launchInCoroutineScope
import ru.mamykin.foboreader.core.presentation.BaseViewModel
import ru.mamykin.foboreader.core.presentation.StringOrResource
import ru.mamykin.foboreader.store.R
import ru.mamykin.foboreader.store.categories.BookCategoryUIModel
import ru.mamykin.foboreader.store.categories.StoreMainViewModel
import ru.mamykin.foboreader.store.list.StoreBookUIModel
import javax.inject.Inject

@HiltViewModel
internal class StoreSearchViewModel @Inject constructor(
    private val searchInStoreUseCase: SearchInStoreUseCase,
) : BaseViewModel<StoreSearchViewModel.Intent, StoreSearchViewModel.State, StoreMainViewModel.Effect>(
    State("", SearchState.NotStarted)
) {
    private var curSearchJob: Job? = null

    override suspend fun handleIntent(intent: Intent) {
        when (intent) {
            is Intent.Search -> runSearch(intent)
            is Intent.RetrySearch -> handleIntent(Intent.Search(state.searchQuery))
        }
    }

    private suspend fun runSearch(intent: Intent.Search) {
        curSearchJob?.cancel()
        if (intent.searchQuery.isEmpty()) {
            state = state.copy(
                searchQuery = intent.searchQuery,
                searchState = SearchState.NotStarted,
            )
        } else {
            curSearchJob = launchInCoroutineScope {
                searchInStore(intent.searchQuery)
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
                val results = mutableListOf<SearchState.SearchResult>()
                if (it.categories.isNotEmpty()) {
                    results.add(
                        SearchState.SearchResult.SectionTitle(
                            StringOrResource.Resource(
                                R.string.bs_search_categories_section_title
                            )
                        )
                    )
                    results.addAll(
                        it.categories.map(BookCategoryUIModel::fromDomainModel).map(SearchState.SearchResult::Category)
                    )
                }
                if (it.books.isNotEmpty()) {
                    results.add(
                        SearchState.SearchResult.SectionTitle(
                            StringOrResource.Resource(
                                R.string.bs_search_books_section_title
                            )
                        )
                    )
                    results.addAll(it.books.map(StoreBookUIModel::fromDomainModel).map(SearchState.SearchResult::Book))
                }
                state = state.copy(
                    searchState = SearchState.Loaded(results)
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
            val results: List<SearchResult>,
        ) : SearchState()

        sealed class SearchResult {
            data class SectionTitle(val title: StringOrResource) : SearchResult()
            data class Category(val category: BookCategoryUIModel) : SearchResult()
            data class Book(val book: StoreBookUIModel) : SearchResult()
        }
    }
}