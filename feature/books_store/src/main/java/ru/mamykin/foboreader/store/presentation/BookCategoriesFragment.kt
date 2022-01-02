package ru.mamykin.foboreader.store.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.afollestad.recyclical.datasource.dataSourceTypedOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.extension.showSnackbar
import ru.mamykin.foboreader.core.presentation.autoCleanedValue
import ru.mamykin.foboreader.store.R
import ru.mamykin.foboreader.store.databinding.FragmentBookCategoriesBinding
import ru.mamykin.foboreader.store.databinding.ItemCategoryBinding
import ru.mamykin.foboreader.store.di.DaggerBookCategoriesComponent
import ru.mamykin.foboreader.store.domain.model.StoreBookCategory
import ru.mamykin.foboreader.store.presentation.list.CategoryViewHolder
import javax.inject.Inject

class BookCategoriesFragment : Fragment(R.layout.fragment_book_categories) {

    companion object {

        fun newInstance(): Fragment = BookCategoriesFragment()
    }

    @Inject
    internal lateinit var feature: BookCategoriesFeature

    private val categoriesSource = dataSourceTypedOf<StoreBookCategory>()
    private val binding by autoCleanedValue { FragmentBookCategoriesBinding.bind(requireView()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerBookCategoriesComponent.factory().create(
            apiHolder().commonApi(),
            apiHolder().networkApi(),
            apiHolder().navigationApi(),
        ).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initErrorView()
        initToolbar()
        initCategoriesList()
        feature.stateData.observe(viewLifecycleOwner, ::showState)
        feature.effectData.observe(viewLifecycleOwner, ::takeEffect)
    }

    private fun initErrorView() {
        binding.vError.setRetryClickListener {
            feature.sendEvent(BookCategories.Event.RetryLoadClicked)
        }
    }

    private fun initToolbar() {
        binding.vToolbar.toolbar.apply {
            title = getString(R.string.books_store_title)
            navigationIcon = null
        }
    }

    private fun initCategoriesList() {
        binding.rvCategories.setup {
            withDataSource(categoriesSource)
            withItem<StoreBookCategory, CategoryViewHolder>(R.layout.item_category) {
                onBind({
                    CategoryViewHolder(ItemCategoryBinding.bind(it))
                }) { _, item ->
                    bind(item)
                }
                onClick {
                    feature.sendEvent(BookCategories.Event.CategoryClicked(item.id))
                }
            }
        }
    }

    private fun showState(state: BookCategories.ViewState) {
        binding.pbLoadingCategories.isVisible = state.isLoading
        binding.vError.isVisible = state.isError
        state.categories?.let(categoriesSource::set)
    }

    private fun takeEffect(effect: BookCategories.Effect) {
        when (effect) {
            is BookCategories.Effect.ShowSnackbar -> showSnackbar(effect.message)
        }
    }
}