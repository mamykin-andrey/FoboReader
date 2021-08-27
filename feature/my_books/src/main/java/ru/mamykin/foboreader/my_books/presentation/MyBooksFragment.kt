package ru.mamykin.foboreader.my_books.presentation

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.annotation.IdRes
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.afollestad.recyclical.datasource.dataSourceTypedOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import reactivecircus.flowbinding.android.view.clicks
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.core.extension.getSearchView
import ru.mamykin.foboreader.core.extension.queryChanges
import ru.mamykin.foboreader.core.presentation.NewBaseFragment
import ru.mamykin.foboreader.core.presentation.viewBinding
import ru.mamykin.foboreader.my_books.R
import ru.mamykin.foboreader.my_books.databinding.FragmentMyBooksBinding
import ru.mamykin.foboreader.my_books.databinding.ItemBookBinding
import ru.mamykin.foboreader.my_books.domain.model.SortOrder
import ru.mamykin.foboreader.my_books.presentation.list.BookViewHolder

class MyBooksFragment : NewBaseFragment(R.layout.fragment_my_books) {

    private val viewModel: MyBooksViewModel by viewModel()
//    private val router: Router by inject()
    private val booksSource = dataSourceTypedOf<BookInfo>()
    private val binding by viewBinding { FragmentMyBooksBinding.bind(requireView()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initBooksList()
        initViewModel()
    }

    private fun initToolbar() = toolbar!!.apply {
        title = getString(R.string.my_books_screen_title)
        navigationIcon = null
        inflateMenu(R.menu.menu_books_list)
        bindItemClick(menu, R.id.actionSortName, SortOrder.ByName)
        bindItemClick(menu, R.id.actionSortReaded, SortOrder.ByReaded)
        bindItemClick(menu, R.id.actionSortDate, SortOrder.ByDate)
        initSearchView(menu)
    }

    private fun bindItemClick(menu: Menu, @IdRes itemId: Int, sortOrder: SortOrder) {
        menu.findItem(itemId)
            .clicks()
            .onEach { viewModel.sendEvent(Event.SortBooks(sortOrder)) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initSearchView(menu: Menu) = menu.getSearchView(R.id.action_search).apply {
        queryHint = getString(R.string.my_books_menu_search)
        queryChanges()
            .filterNotNull()
            .onEach { viewModel.sendEvent(Event.FilterBooks(it)) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initBooksList() {
        binding.rvMyBooks.setup {
            withEmptyView(binding.vNoBooks)
            withDataSource(booksSource)
            withItem<BookInfo, BookViewHolder>(R.layout.item_book) {
                onBind({
                    BookViewHolder(
                        ItemBookBinding.bind(it),
                        { TODO("Not implemented") }
                    ) { viewModel.sendEvent(Event.RemoveBook(it)) }
                }) { _, item -> bind(item) }
                onClick { TODO("Not implemented") }
            }
        }
    }

    private fun initViewModel() {
        viewModel.stateLiveData.observe(viewLifecycleOwner, ::showState)
    }

    private fun showState(state: ViewState) {
        progressView.isVisible = state.isLoading
        booksSource.set(state.books)
    }
}