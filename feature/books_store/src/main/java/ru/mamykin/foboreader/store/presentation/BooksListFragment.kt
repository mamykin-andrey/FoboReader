package ru.mamykin.foboreader.store.presentation

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.mamykin.foboreader.core.di.ComponentHolder
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.extension.getSearchView
import ru.mamykin.foboreader.core.extension.queryChanges
import ru.mamykin.foboreader.core.extension.showSnackbar
import ru.mamykin.foboreader.core.presentation.BaseFragment
import ru.mamykin.foboreader.core.presentation.autoCleanedValue
import ru.mamykin.foboreader.store.R
import ru.mamykin.foboreader.store.databinding.FragmentBooksListBinding
import ru.mamykin.foboreader.store.di.DaggerBookListComponent
import ru.mamykin.foboreader.store.presentation.list.BookListAdapter
import javax.inject.Inject

internal class BooksListFragment : BaseFragment(R.layout.fragment_books_list) {

    companion object {

        private const val EXTRA_CATEGORY_ID = "extra_category_id"

        fun newInstance(categoryId: String): Fragment = BooksListFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_CATEGORY_ID, categoryId)
            }
        }
    }

    override val featureName: String = "books_list"

    @Inject
    internal lateinit var feature: BooksListFeature

    private val adapter by lazy {
        BookListAdapter { link, fileName ->
            feature.sendEvent(BooksList.Event.DownloadBookClicked(link, fileName))
        }
    }
    private val binding by autoCleanedValue { FragmentBooksListBinding.bind(requireView()) }
    private val categoryId by lazy { requireArguments().getString(EXTRA_CATEGORY_ID)!! }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDi()
    }

    private fun initDi() {
        ComponentHolder.getOrCreateComponent(featureName) {
            val params = BooksListFeature.BookCategoriesParams(categoryId)
            DaggerBookListComponent.factory().create(
                apiHolder().commonApi(),
                apiHolder().networkApi(),
                apiHolder().navigationApi(),
                apiHolder().settingsApi(),
                params,
            )
        }.inject(this)
    }

    override fun onCleared() {
        feature.onCleared()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initErrorView()
        initToolbar()
        initBooksList()
        feature.stateData.observe(viewLifecycleOwner, ::showState)
        feature.effectData.observe(viewLifecycleOwner, ::takeEffect)
    }

    override fun onDestroyView() {
        binding.rvBooks.adapter = null
        super.onDestroyView()
    }

    private fun initErrorView() {
        binding.vError.setRetryClickListener {
            feature.sendEvent(BooksList.Event.RetryBooksClicked)
        }
    }

    private fun initToolbar() {
        val rawIconBackDrawable = requireContext().getDrawable(R.drawable.ic_back)!!
        val iconBackDrawable = rawIconBackDrawable.mutate()
        iconBackDrawable.setTint(resources.getColor(R.color.colorSecondary))
        binding.toolbar.apply {
            title = getString(R.string.books_store_title)
            navigationIcon = iconBackDrawable
            inflateMenu(R.menu.menu_books_store)
            initSearchView(menu)
            setNavigationOnClickListener { requireActivity().onBackPressed() }
        }
    }

    private fun initBooksList() {
        binding.rvBooks.adapter = adapter
    }

    private fun initSearchView(menu: Menu) = menu.getSearchView(R.id.action_search).apply {
        queryHint = getString(R.string.books_store_menu_search)
        queryChanges()
            .filterNotNull()
            .onEach { feature.sendEvent(BooksList.Event.FilterQueryChanged(it)) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun showState(state: BooksList.ViewState) {
        binding.pbLoadingBooks.isVisible = state.isLoading
        binding.vError.isVisible = state.isError
        state.books?.let {
            binding.rvBooks.isVisible = true
            adapter.submitList(it)
        } ?: run {
            binding.rvBooks.isVisible = false
        }
    }

    private fun takeEffect(effect: BooksList.Effect) {
        when (effect) {
            is BooksList.Effect.ShowSnackbar -> showSnackbar(effect.message)
        }
    }
}