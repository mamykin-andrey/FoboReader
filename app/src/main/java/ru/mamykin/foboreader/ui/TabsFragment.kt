package ru.mamykin.foboreader.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.fragment_tabs.*
import ru.mamykin.core.ui.BaseFragment
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.navigation.KeepStateNavigator
import ru.mamykin.my_books.presentation.my_books.MyBooksFragment

class TabsFragment : BaseFragment(R.layout.fragment_tabs) {

    private val navController by lazy { activity!!.findNavController(R.id.fr_tabs_host) }
    private val navHostFragment by lazy { childFragmentManager.findFragmentById(R.id.fr_tabs_host) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBottomNavigationView()
    }

    fun openMyBooksTab() {
        bnv_tabs.selectedItemId = R.id.myBooksFragment
        navHostFragment!!.childFragmentManager
                .fragments
                .firstOrNull()
                ?.let { it as? MyBooksFragment }
                ?.scanBooks()
    }

    private fun initBottomNavigationView() {
        val navigator = KeepStateNavigator(
                context!!,
                navHostFragment!!.childFragmentManager,
                R.id.fr_tabs_host
        )
        navController.navigatorProvider.addNavigator(navigator)
        navController.setGraph(R.navigation.tabs)
        bnv_tabs.setupWithNavController(navController)
    }
}