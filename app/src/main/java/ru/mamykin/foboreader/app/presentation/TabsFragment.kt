package ru.mamykin.foboreader.app.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.presentation.viewBinding
import ru.mamykin.foboreader.databinding.FragmentTabsBinding

class TabsFragment : Fragment(R.layout.fragment_tabs) {

    private val binding by viewBinding { FragmentTabsBinding.bind(requireView()) }
    private val viewModel: TabsViewModel by viewModel()
    private val currentTabFragment: Fragment?
        get() = childFragmentManager.fragments.firstOrNull { !it.isHidden }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigationBar()

        if (savedInstanceState == null) {
            binding.bnvTabs.selectedItemId = R.id.my_books
        }
    }

    override fun onDestroyView() {
        viewModel.selectedItemIdData.value = binding.bnvTabs.selectedItemId
        super.onDestroyView()
    }

    private fun setupNavigationBar() {
        binding.bnvTabs.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.my_books -> selectTab(Tab.MyBooksTab)
                R.id.books_store -> selectTab(Tab.BooksStoreTab)
                R.id.settings -> selectTab(Tab.SettingsTab)
            }
            true
        }
    }

    private fun selectTab(tab: Tab) {
        val currentFragment = currentTabFragment
        val newFragment = childFragmentManager.findFragmentByTag(tab.tag)

        if (currentFragment != null && newFragment != null && currentFragment == newFragment) return

        childFragmentManager.beginTransaction().apply {
            if (newFragment == null) add(R.id.fr_tabs_host, createTabFragment(tab), tab.tag)

            currentFragment?.let {
                hide(it)
                it.userVisibleHint = false
            }
            newFragment?.let {
                show(it)
                it.userVisibleHint = true
            }
        }.commitNow()
    }

    private fun createTabFragment(tab: Tab) = tab.newFragment()

    // TODO:
//    override fun onBackPressed() {
//        val currentFragment = (supportFragmentManager.findFragmentByTag(currentTab) as TabNavigationFragment)
//        if (!currentFragment.onBackPressed())
//            finish()
//    }
}