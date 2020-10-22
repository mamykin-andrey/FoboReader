package ru.mamykin.foboreader.app.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import org.koin.android.ext.android.inject
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.app.navigation.AppNavigator
import ru.mamykin.foboreader.core.extension.setupWithNavController
import ru.mamykin.foboreader.core.presentation.viewBinding
import ru.mamykin.foboreader.databinding.FragmentTabsBinding

class TabsFragment : Fragment(R.layout.fragment_tabs) {

    private val appNavigator: AppNavigator by inject()
    private val binding by viewBinding { FragmentTabsBinding.bind(requireView()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBottomNavigationView()
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() } // for correct transition in nested fragments
        appNavigator.tabsNavigationView = binding.bnvTabs
    }

    private fun initBottomNavigationView() {
        binding.bnvTabs.setupWithNavController(
            navGraphIds = listOf(
                R.navigation.my_books,
                R.navigation.books_store,
                R.navigation.settings
            ),
            fragmentManager = childFragmentManager,
            containerId = R.id.fr_tabs_host,
            intent = requireActivity().intent
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        appNavigator.tabsNavigationView = null
    }
}