package ru.mamykin.foboreader.app.presentation.tabs

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.navigation.BackPressedListener
import ru.mamykin.foboreader.core.presentation.autoCleanedValue
import ru.mamykin.foboreader.databinding.FragmentTabsBinding

class TabsFragment : Fragment(R.layout.fragment_tabs) {

    private val binding by autoCleanedValue { FragmentTabsBinding.bind(requireView()) }

    private val currentTabFragment: Fragment?
        get() = childFragmentManager.fragments.firstOrNull { it.isVisible }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigationBar()

        val tab = when (currentTabFragment?.tag) {
            Tab.BooksStore.tag -> Tab.BooksStore
            Tab.Settings.tag -> Tab.Settings
            else -> Tab.MyBooks
        }
        selectTab(tab)
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

    private fun selectTab(tab: Tab) {
        val currentFragment = currentTabFragment
        val fragmentToShow = childFragmentManager.findFragmentByTag(tab.tag)
        if (currentFragment != null && currentFragment == fragmentToShow) return

        val transaction = childFragmentManager.beginTransaction()
        fragmentToShow ?: run {
            transaction.add(
                R.id.fr_tabs_host,
                tab.newFragment(),
                tab.tag
            )
        }
        currentFragment?.let(transaction::hide)
        fragmentToShow?.let(transaction::show)
        transaction.commitNow()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (currentTabFragment as BackPressedListener).onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}