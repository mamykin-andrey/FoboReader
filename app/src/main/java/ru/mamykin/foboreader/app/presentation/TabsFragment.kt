package ru.mamykin.foboreader.app.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.app.navigation.AppNavigator
import ru.mamykin.foboreader.core.extension.setupWithNavController
import ru.mamykin.foboreader.core.presentation.viewBinding
import ru.mamykin.foboreader.databinding.FragmentTabsBinding

class TabsFragment : Fragment(R.layout.fragment_tabs) {

    private val appNavigator: AppNavigator by inject()
    private val binding by viewBinding { FragmentTabsBinding.bind(requireView()) }
    private val viewModel: TabsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBottomNavigationView()
        appNavigator.tabsNavigationView = binding.bnvTabs
    }

    private fun initBottomNavigationView() = binding.bnvTabs.apply {
        viewModel.selectedItemIdData.value?.let { selectedItemId = it }
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
        viewModel.selectedItemIdData.value = binding.bnvTabs.selectedItemId
        super.onDestroyView()
    }
}