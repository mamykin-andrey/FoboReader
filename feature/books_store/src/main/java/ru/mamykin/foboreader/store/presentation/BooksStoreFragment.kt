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
import ru.mamykin.foboreader.core.presentation.NewBaseFragment
import ru.mamykin.foboreader.core.presentation.viewBinding
import ru.mamykin.foboreader.store.R
import ru.mamykin.foboreader.store.databinding.FragmentBooksStoreBinding
import ru.mamykin.foboreader.store.databinding.ItemStoreBookBinding
import ru.mamykin.foboreader.store.domain.model.StoreBook
import ru.mamykin.foboreader.store.presentation.list.StoreBookViewHolder

class BooksStoreFragment : NewBaseFragment(R.layout.fragment_books_store) {

    private val feature: BooksStoreFeature by viewModel()
    private val booksSource = dataSourceTypedOf<StoreBook>()
    private val binding by viewBinding { FragmentBooksStoreBinding.bind(requireView()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initErrorView()
        initToolbar()
        initBooksList()
        feature.stateData.observe(viewLifecycleOwner, ::showState)
        feature.effectData.observe(viewLifecycleOwner, ::takeEffect)
    }

    private fun initErrorView() {
        binding.vError.setOnRetryClickListener {
            feature.sendEvent(BooksStore.Event.RetryBooksClicked)
        }
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
                onBind({
                    StoreBookViewHolder(ItemStoreBookBinding.bind(it))
                }) { _, item ->
                    bind(item)
                }
                onClick {
                    feature.sendEvent(BooksStore.Event.DownloadBookClicked(item))
                }
            }
        }
    }

    private fun initSearchView(menu: Menu) = menu.getSearchView(R.id.action_search).apply {
        queryHint = getString(R.string.books_store_menu_search)
        queryChanges()
            .filterNotNull()
            .onEach { feature.sendEvent(BooksStore.Event.FilterQueryChanged(it)) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun showState(state: BooksStore.ViewState) {
        progressView.isVisible = state.isLoading
        binding.vError.isVisible = state.isError
        booksSource.set(state.books)
    }

    private fun takeEffect(effect: BooksStore.Effect) {
        when (effect) {
            is BooksStore.Effect.ShowSnackbar -> showSnackbar(effect.message)
        }
    }
}