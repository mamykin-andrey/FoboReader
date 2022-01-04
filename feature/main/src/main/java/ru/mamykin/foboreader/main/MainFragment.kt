package ru.mamykin.foboreader.main

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.mamykin.foboreader.core.extension.addBackPressedDispatcher
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.presentation.autoCleanedValue
import ru.mamykin.foboreader.main.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    private val tagsToNavIdsMap = mapOf(
        Tab.MyBooks.tag to R.id.my_books,
        Tab.BooksStore.tag to R.id.books_store,
        Tab.Settings.tag to R.id.settings,
    )
    private val binding by autoCleanedValue { FragmentMainBinding.bind(requireView()) }
    private val tabFragmentProvider by lazy { apiHolder().mainApi().tabFragmentProvider() }
    private var isFirstLaunch = true

    override fun onAttach(context: Context) {
        super.onAttach(context)
        addBackPressedDispatcher {
            if (childFragmentManager.popBackStackImmediate()) {
                binding.bnvTabs.selectedItemId = getSelectedNavId()
            } else {
                requireActivity().finish()
            }
        }
    }

    private fun getSelectedNavId(): Int {
        val currentTabFragment = getCurrentTabFragment()
            ?: throw NullPointerException("No visible fragment found!")
        val currentTabFragmentTag = currentTabFragment.tag
            ?: throw NullPointerException("Current fragment doesn't have tag!")

        return tagsToNavIdsMap[currentTabFragmentTag]
            ?: throw NullPointerException("Unknown fragment tag: $currentTabFragmentTag!")
    }

    private fun getCurrentTabFragment(): Fragment? {
        return childFragmentManager.fragments.firstOrNull { it.isVisible }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isFirstLaunch = savedInstanceState == null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigationBar()

        if (isFirstLaunch) {
            isFirstLaunch = false
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
        val currentFragment = getCurrentTabFragment()
        val fragmentToShow = childFragmentManager.findFragmentByTag(tab.tag)
        if (currentFragment != null && currentFragment == fragmentToShow) return

        val transaction = childFragmentManager.beginTransaction()
        if (fragmentToShow == null) {
            transaction.add(
                R.id.fr_tabs_host,
                tab.newFragment(tabFragmentProvider),
                tab.tag,
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