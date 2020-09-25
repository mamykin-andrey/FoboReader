package ru.mamykin.foboreader.app.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.fragment_tabs.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.navigation.KeepStateNavigator
import ru.mamykin.foboreader.my_books.presentation.MyBooksFragment

class TabsFragment : Fragment(R.layout.fragment_tabs) {

    private val navController by lazy { activity!!.findNavController(R.id.fr_tabs_host) }
    private val navHostFragment by lazy { childFragmentManager.findFragmentById(R.id.fr_tabs_host) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBottomNavigationView()
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() } // for correct transition in nested fragments
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