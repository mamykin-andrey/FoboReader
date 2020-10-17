package ru.mamykin.foboreader.app.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_tabs.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.extension.setupWithNavController

class TabsFragment : Fragment(R.layout.fragment_tabs) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBottomNavigationView()
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() } // for correct transition in nested fragments
    }

    private fun initBottomNavigationView() {
        bnv_tabs.setupWithNavController(
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
}