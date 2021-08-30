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
import javax.inject.Inject

class TabsFragment : Fragment(R.layout.fragment_tabs) {

    private val binding by autoCleanedValue { FragmentTabsBinding.bind(requireView()) }

    @Inject
    lateinit var viewModel: TabsViewModel

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
        val fm = childFragmentManager
        var currentFragment: Fragment? = null
        val fragments = fm.fragments
        for (f in fragments) {
            if (f.isVisible) {
                currentFragment = f
                break
            }
        }
        val newFragment = fm.findFragmentByTag(tab.tag)
        if (currentFragment != null && newFragment != null && currentFragment === newFragment) return
        val transaction = fm.beginTransaction()
        if (newFragment == null) {
            transaction.add(
                R.id.fr_tabs_host,
                tab.newFragment(), tab.tag
            )
        }
        if (currentFragment != null) {
            transaction.hide(currentFragment)
        }
        if (newFragment != null) {
            transaction.show(newFragment)
        }
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