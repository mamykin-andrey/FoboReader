package ru.mamykin.foboreader.my_books.presentation

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.annotation.IdRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Router
import ru.mamykin.foboreader.core.di.ComponentHolder
import ru.mamykin.foboreader.core.extension.*
import ru.mamykin.foboreader.core.navigation.ScreenProvider
import ru.mamykin.foboreader.core.presentation.BaseFragment
import ru.mamykin.foboreader.core.presentation.autoCleanedValue
import ru.mamykin.foboreader.my_books.R
import ru.mamykin.foboreader.my_books.databinding.FragmentMyBooksBinding
import ru.mamykin.foboreader.my_books.di.DaggerMyBooksComponent
import ru.mamykin.foboreader.my_books.domain.model.SortOrder
import ru.mamykin.foboreader.my_books.presentation.list.BookAdapter
import javax.inject.Inject

class MyBooksFragment : BaseFragment(R.layout.fragment_my_books) {

    companion object {

        fun newInstance(): Fragment = MyBooksFragment()
    }

    override val featureName: String = "my_books"

    @Inject
    internal lateinit var feature: MyBooksFeature

    @Inject
    internal lateinit var router: Router

    @Inject
    internal lateinit var screenProvider: ScreenProvider

    private val binding by autoCleanedValue { FragmentMyBooksBinding.bind(requireView()) }
    private val adapter by lazy {
        BookAdapter(
            { router.navigateTo(screenProvider.readBookScreen(it)) },
            { router.navigateTo(screenProvider.bookDetailsScreen(it)) },
            { feature.sendEvent(MyBooksFeature.Event.RemoveBookClicked(it)) }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDi()
    }

    private fun initDi() {
        ComponentHolder.getOrCreateComponent(featureName) {
            DaggerMyBooksComponent.factory().create(
                apiHolder().navigationApi(),
                commonApi(),
            )
        }.inject(this)
    }

    override fun onCleared() {
        feature.onCleared()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initBooksList()
        initFeature()
    }

    override fun onDestroyView() {
        binding.rvMyBooks.adapter = null
        super.onDestroyView()
    }

    private fun initToolbar() = binding.toolbar.apply {
        title = getString(R.string.my_books_screen_title)
        navigationIcon = null
        inflateMenu(R.menu.menu_books_list)
        bindItemClick(menu, R.id.actionSortName, SortOrder.ByName)
        bindItemClick(menu, R.id.actionSortReaded, SortOrder.ByReaded)
        bindItemClick(menu, R.id.actionSortDate, SortOrder.ByDate)
        initSearchView(menu)
    }

    private fun bindItemClick(menu: Menu, @IdRes itemId: Int, sortOrder: SortOrder) {
        menu.findItem(itemId).setItemClickedListener {
            feature.sendEvent(MyBooksFeature.Event.SortOrderChanged(sortOrder))
            true
        }
    }

    private fun initSearchView(menu: Menu) {
        val searchView = menu.getSearchView(R.id.action_search)
        searchView.queryHint = getString(R.string.my_books_menu_search)
        searchView.setQueryChangedListener {
            feature.sendEvent(MyBooksFeature.Event.FilterTextChanged(it))
        }
    }

    private fun initBooksList() {
        binding.rvMyBooks.adapter = adapter
    }

    private fun initFeature() {
        feature.stateFlow.collectWithRepeatOnStarted(viewLifecycleOwner, ::showState)
        feature.effectData.observe(viewLifecycleOwner, ::showState)
    }

    private fun showState(state: MyBooksFeature.State) = with(binding) {
        pbLoadingBooks.isVisible = state.isLoading
        state.books?.let {
            adapter.submitList(it)
            showNoBooks(it.isEmpty())
        }
    }

    private fun showNoBooks(noBooks: Boolean) = with(binding) {
        vNoBooks.isVisible = noBooks
        rvMyBooks.isVisible = !noBooks
    }
}