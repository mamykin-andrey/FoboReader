package ru.mamykin.foboreader.store.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import ru.mamykin.foboreader.core.di.ComponentHolder
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.extension.commonApi
import ru.mamykin.foboreader.core.extension.showSnackbar
import ru.mamykin.foboreader.core.presentation.BaseFragment
import ru.mamykin.foboreader.core.presentation.autoCleanedValue
import ru.mamykin.foboreader.store.R
import ru.mamykin.foboreader.store.databinding.FragmentBookCategoriesBinding
import ru.mamykin.foboreader.store.di.DaggerBookCategoriesComponent
import ru.mamykin.foboreader.store.presentation.list.CategoryListAdapter
import javax.inject.Inject

class BookCategoriesFragment : BaseFragment(R.layout.fragment_book_categories) {

    companion object {
        fun newInstance(): Fragment = BookCategoriesFragment()
    }

    override val featureName: String = "book_categories"

    @Inject
    internal lateinit var feature: BookCategoriesFeature

    private val adapter: CategoryListAdapter by lazy {
        CategoryListAdapter {
            feature.sendIntent(BookCategoriesFeature.Intent.OpenCategory(it))
        }
    }
    private val binding by autoCleanedValue { FragmentBookCategoriesBinding.bind(requireView()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDi()
    }

    private fun initDi() {
        ComponentHolder.getOrCreateComponent(featureName) {
            DaggerBookCategoriesComponent.factory().create(
                commonApi(),
                apiHolder().networkApi(),
                apiHolder().navigationApi(),
                apiHolder().settingsApi(),
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
        initCategoriesList()
        feature.stateData.observe(viewLifecycleOwner, ::showState)
        feature.effectData.observe(viewLifecycleOwner, ::takeEffect)
    }

    override fun onDestroyView() {
        binding.rvCategories.adapter = null
        super.onDestroyView()
    }

    private fun initErrorView() {
        binding.vError.setRetryClickListener {
            feature.sendIntent(BookCategoriesFeature.Intent.LoadCategories)
        }
    }

    private fun initToolbar() {
        binding.toolbar.apply {
            title = getString(R.string.books_store_title)
            navigationIcon = null
        }
    }

    private fun initCategoriesList() {
        binding.rvCategories.adapter = adapter
    }

    private fun showState(state: BookCategoriesFeature.State) {
        binding.pbLoadingCategories.isVisible = state.isLoading
        binding.vError.isVisible = state.isError
        state.categories?.let { adapter.submitList(it) }
    }

    private fun takeEffect(effect: BookCategoriesFeature.Effect) {
        when (effect) {
            is BookCategoriesFeature.Effect.ShowSnackbar -> showSnackbar(effect.message)
        }
    }
}