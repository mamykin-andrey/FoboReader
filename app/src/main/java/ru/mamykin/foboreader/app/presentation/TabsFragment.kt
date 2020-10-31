package ru.mamykin.foboreader.app.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import org.koin.android.ext.android.inject
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.app.navigation.AppNavigator
import ru.mamykin.foboreader.core.extension.setupWithNavController
import ru.mamykin.foboreader.core.presentation.viewBinding
import ru.mamykin.foboreader.databinding.FragmentTabsBinding

class TabsFragment : Fragment(R.layout.fragment_tabs) {

    companion object {
        // TODO: remove this, save selected item id in viewModel
        private const val EXTRA_SAVED_SELECTED_ITEM_ID = "saved_selected_item_id"
    }

    private val appNavigator: AppNavigator by inject()
    private val binding by viewBinding { FragmentTabsBinding.bind(requireView()) }
    private var savedSelectedItemId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedSelectedItemId = savedInstanceState?.getInt(EXTRA_SAVED_SELECTED_ITEM_ID)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBottomNavigationView()
        appNavigator.tabsNavigationView = binding.bnvTabs
    }

    private fun initBottomNavigationView() = binding.bnvTabs.apply {
        savedSelectedItemId?.let { selectedItemId = it }
        setupWithNavController(
            this@TabsFragment,
            listOf(
                R.navigation.my_books,
                R.navigation.books_store,
                R.navigation.settings
            ),
            R.id.fr_tabs_host
        )
    }

    override fun onDestroyView() {
        appNavigator.tabsNavigationView = null
        savedSelectedItemId = binding.bnvTabs.selectedItemId
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val savedSelectedItemId = view?.let { binding.bnvTabs.selectedItemId } ?: this@TabsFragment.savedSelectedItemId
        savedSelectedItemId?.let { outState.putInt(EXTRA_SAVED_SELECTED_ITEM_ID, it) }
    }
}