package ru.mamykin.foboreader.store.presentation

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.afollestad.recyclical.datasource.dataSourceTypedOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mamykin.foboreader.core.extension.getSearchView
import ru.mamykin.foboreader.core.extension.queryChanges
import ru.mamykin.foboreader.core.extension.showSnackbar
import ru.mamykin.foboreader.core.presentation.BaseFragment
import ru.mamykin.foboreader.core.presentation.viewBinding
import ru.mamykin.foboreader.store.R
import ru.mamykin.foboreader.store.databinding.FragmentBooksStoreBinding
import ru.mamykin.foboreader.store.databinding.ItemStoreBookBinding
import ru.mamykin.foboreader.store.domain.model.StoreBook
import ru.mamykin.foboreader.store.presentation.list.StoreBookViewHolder

class BooksStoreFragment : BaseFragment<BooksStoreViewModel, ViewState, Effect>(R.layout.fragment_books_store) {

    override val viewModel: BooksStoreViewModel by viewModel()

    private val booksSource = dataSourceTypedOf<StoreBook>()
    private val binding by viewBinding { FragmentBooksStoreBinding.bind(requireView()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initErrorView()
        initToolbar()
        initBooksList()
    }

    private fun initErrorView() {
        binding.vError.setOnRetryClickListener { viewModel.sendEvent(Event.RetryBooksLoading) }
    }

    private fun initToolbar() {
        toolbar!!.apply {
            title = getString(R.string.books_store_title)
            navigationIcon = null
            inflateMenu(R.menu.menu_books_store)
            initSearchView(menu)
        }
    }

    private fun initBooksList() {
        binding.rvBooks.setup {
            withDataSource(booksSource)
            withItem<StoreBook, StoreBookViewHolder>(R.layout.item_store_book) {
                onBind({ StoreBookViewHolder(ItemStoreBookBinding.bind(it)) }) { _, item -> bind(item) }
                onClick { viewModel.sendEvent(Event.DownloadBook(item)) }
            }
        }
    }

    private fun initSearchView(menu: Menu) = menu.getSearchView(R.id.action_search).apply {
        queryHint = getString(R.string.books_store_menu_search)
        queryChanges()
            .filterNotNull()
            .onEach { viewModel.sendEvent(Event.FilterBooks(it)) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun showState(state: ViewState) {
        progressView.isVisible = state.isLoading
        binding.vError.isVisible = state.isError
        booksSource.set(state.books)
    }

    override fun takeEffect(effect: Effect) {
        when (effect) {
            is Effect.ShowSnackbar -> showSnackbar(effect.message)
        }
    }
}