package ru.mamykin.foboreader.app.presentation.tabs

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.presentation.autoCleanedValue
import ru.mamykin.foboreader.databinding.FragmentTabsBinding

class TabsFragment : Fragment(R.layout.fragment_tabs) {

    private val navIdsToTabs = mapOf(
        R.id.my_books to Tab.MyBooks,
        R.id.books_store to Tab.BooksStore,
        R.id.settings to Tab.Settings
    )
    private val binding by autoCleanedValue { FragmentTabsBinding.bind(requireView()) }

    private val currentTabFragment: Fragment?
        get() = childFragmentManager.fragments.firstOrNull { it.isVisible }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (childFragmentManager.popBackStackImmediate()) {
                    binding.bnvTabs.selectedItemId = getSelectedNavId()
                } else {
                    requireActivity().finish()
                }
            }
        })
    }

    private fun getSelectedNavId(): Int {
        val tag = currentTabFragment!!.tag!!
        return navIdsToTabs.toList().find { it.second.tag == tag }!!.first
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigationBar()

        if (savedInstanceState == null) {
            selectTab(Tab.MyBooks, addToBackStack = false)
        }
    }

    private fun setupNavigationBar() {
        binding.bnvTabs.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.my_books -> selectTab(Tab.MyBooks)
                R.id.books_store -> selectTab(Tab.BooksStore)
                R.id.settings -> selectTab(Tab.Settings)
            }
            true
        }
    }

    private fun selectTab(tab: Tab, addToBackStack: Boolean = true) {
        val currentFragment = currentTabFragment
        val fragmentToShow = childFragmentManager.findFragmentByTag(tab.tag)
        if (currentFragment != null && currentFragment == fragmentToShow) return

        val transaction = childFragmentManager.beginTransaction()
        if (fragmentToShow == null) {
            transaction.add(
                R.id.fr_tabs_host,
                tab.newFragment(),
                tab.tag
            )
        }
        currentFragment?.let(transaction::hide)
        fragmentToShow?.let(transaction::show)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }
}