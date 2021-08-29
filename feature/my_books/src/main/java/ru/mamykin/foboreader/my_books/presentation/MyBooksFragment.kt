package ru.mamykin.foboreader.my_books.presentation

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.annotation.IdRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ListAdapter
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import reactivecircus.flowbinding.android.view.clicks
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.core.extension.getSearchView
import ru.mamykin.foboreader.core.extension.queryChanges
import ru.mamykin.foboreader.core.navigation.screen.ReadBookScreen
import ru.mamykin.foboreader.core.presentation.viewBinding
import ru.mamykin.foboreader.my_books.R
import ru.mamykin.foboreader.my_books.databinding.FragmentMyBooksBinding
import ru.mamykin.foboreader.my_books.domain.model.SortOrder
import ru.mamykin.foboreader.my_books.presentation.list.BookAdapter
import ru.mamykin.foboreader.my_books.presentation.list.BookViewHolder

class MyBooksFragment : Fragment(R.layout.fragment_my_books) {

    private val viewModel: MyBooksViewModel by viewModel()
    private val binding by viewBinding { FragmentMyBooksBinding.bind(requireView()) }
    private val router: Router by inject()
    private var adapter: ListAdapter<BookInfo, BookViewHolder>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initBooksList()
        initViewModel()
    }

    private fun initToolbar() = binding.vToolbar.toolbar.apply {
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
        adapter = BookAdapter(
            { router.navigateTo(ReadBookScreen(it)) },
            { TODO("Not implemented") },
            { TODO("Not implemented") }
        )
        binding.rvMyBooks.adapter = adapter
    }

    private fun initViewModel() {
        viewModel.stateLiveData.observe(viewLifecycleOwner, ::showState)
    }

    private fun showState(state: ViewState) = with(binding) {
        when (state) {
            is ViewState.Loading -> {
                vProgress.root.isVisible = true

                vNoBooks.isVisible = false
                rvMyBooks.isVisible = false
            }
            is ViewState.Empty -> {
                vNoBooks.isVisible = true

                vProgress.root.isVisible = false
                rvMyBooks.isVisible = false
            }
            is ViewState.Success -> {
                rvMyBooks.isVisible = true
                adapter?.submitList(state.books)

                vProgress.root.isVisible = false
                vNoBooks.isVisible = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvMyBooks.adapter = null
        adapter = null
    }
}