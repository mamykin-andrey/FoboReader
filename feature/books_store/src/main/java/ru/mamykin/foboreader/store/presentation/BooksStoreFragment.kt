package ru.mamykin.foboreader.store.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mamykin.foboreader.core.extension.getSearchView
import ru.mamykin.foboreader.core.extension.isVisible
import ru.mamykin.foboreader.core.extension.queryChanges
import ru.mamykin.foboreader.core.extension.showSnackbar
import ru.mamykin.foboreader.core.ui.BaseFragment
import ru.mamykin.foboreader.store.R
import ru.mamykin.foboreader.store.databinding.FragmentBooksStoreBinding
import ru.mamykin.foboreader.store.presentation.list.BooksStoreRecyclerAdapter

@ExperimentalCoroutinesApi
@FlowPreview
class BooksStoreFragment : BaseFragment<BooksStoreViewModel, ViewState, Effect>() {

    override val viewModel: BooksStoreViewModel by viewModel()

    private val adapter = BooksStoreRecyclerAdapter { viewModel.sendEvent(Event.DownloadBook(it)) }

    private lateinit var binding: FragmentBooksStoreBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBooksStoreBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initErrorView()
        binding.rvBooks.adapter = adapter
        initToolbar()
    }

    private fun initErrorView() {
        binding.vError.setOnRetryClickListener { viewModel.sendEvent(Event.RetryBooksLoading) }
    }

    private fun initToolbar() {
        toolbar.apply {
            title = getString(R.string.books_store_title)
            navigationIcon = null
            inflateMenu(R.menu.menu_books_store)
            initSearchView(menu)
        }
    }

    private fun initSearchView(menu: Menu) = menu.getSearchView(R.id.action_search).apply {
        queryHint = getString(R.string.books_store_menu_search)
        queryChanges()
            .filterNotNull()
            .onEach { viewModel.sendEvent(Event.FilterBooks(it)) }
    }

    override fun showState(state: ViewState) {
        progressView.isVisible = state.isLoading
        binding.vError.isVisible = state.isError
        adapter.changeItems(state.books)
    }

    override fun takeEffect(effect: Effect) {
        when (effect) {
            is Effect.ShowSnackbar -> showSnackbar(effect.message)
        }
    }
}