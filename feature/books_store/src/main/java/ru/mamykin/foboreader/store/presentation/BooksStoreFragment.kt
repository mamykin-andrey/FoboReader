package ru.mamykin.foboreader.store.presentation

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.afollestad.recyclical.datasource.dataSourceTypedOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.mamykin.foboreader.core.extension.getSearchView
import ru.mamykin.foboreader.core.extension.queryChanges
import ru.mamykin.foboreader.core.extension.showSnackbar
import ru.mamykin.foboreader.core.presentation.autoCleanedValue
import ru.mamykin.foboreader.store.R
import ru.mamykin.foboreader.store.databinding.FragmentBooksStoreBinding
import ru.mamykin.foboreader.store.databinding.ItemStoreBookBinding
import ru.mamykin.foboreader.store.di.BooksStoreComponentHolder
import ru.mamykin.foboreader.store.domain.model.StoreBook
import ru.mamykin.foboreader.store.presentation.list.StoreBookViewHolder
import javax.inject.Inject

class BooksStoreFragment : Fragment(R.layout.fragment_books_store) {

    @Inject
    lateinit var feature: BooksStoreFeature

    private val booksSource = dataSourceTypedOf<StoreBook>()
    private val binding by autoCleanedValue { FragmentBooksStoreBinding.bind(requireView()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as BooksStoreComponentHolder).booksStoreComponent().inject(this)
    }

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
        binding.vToolbar.toolbar.apply {
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
        binding.vProgress.flContentLoading.isVisible = state.isLoading
        binding.vError.isVisible = state.isError
        booksSource.set(state.books)
    }

    private fun takeEffect(effect: BooksStore.Effect) {
        when (effect) {
            is BooksStore.Effect.ShowSnackbar -> showSnackbar(effect.message)
        }
    }
}