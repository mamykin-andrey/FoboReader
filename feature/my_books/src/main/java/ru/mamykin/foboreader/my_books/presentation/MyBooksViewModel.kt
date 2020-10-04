package ru.mamykin.foboreader.my_books.presentation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.mvvm.BaseViewModel
import ru.mamykin.foboreader.my_books.domain.interactor.MyBooksInteractor

@FlowPreview
@ExperimentalCoroutinesApi
class MyBooksViewModel constructor(
    private val interactor: MyBooksInteractor
) : BaseViewModel<ViewState, Action, Event, Effect>(
    ViewState(isLoading = true)
) {
    override fun loadData() {
        initBooksFlow()
        launch { interactor.loadBooks() }
    }

    private fun initBooksFlow() = launch {
        interactor.booksFlow.collect {
            sendAction(Action.BooksLoaded(it))
        }
    }

    override fun onAction(action: Action): ViewState = when (action) {
        is Action.Loading -> state.copy(isLoading = true)
        is Action.BooksLoaded -> state.copy(isLoading = false, books = action.books)
    }

    override suspend fun onEvent(event: Event) {
        when (event) {
            is Event.ScanBooks -> scanBooks()
            is Event.LoadBooks -> interactor.loadBooks()
            is Event.RemoveBook -> interactor.removeBook(event.id)
            is Event.SortBooks -> interactor.sortBooks(event.sortOrder)
            is Event.FilterBooks -> interactor.filterByQuery(event.query)
        }
    }

    private suspend fun scanBooks() {
        sendAction(Action.Loading)
        interactor.scanBooks()
    }
}