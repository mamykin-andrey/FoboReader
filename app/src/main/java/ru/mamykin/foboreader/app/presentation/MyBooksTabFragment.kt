package ru.mamykin.foboreader.app.presentation

import android.os.Bundle
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.navigation.TabContainerFragment
import ru.mamykin.foboreader.core.navigation.screen.MyBooksScreen

class MyBooksTabFragment : TabContainerFragment(Tab.MyBooksTab.tag) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (childFragmentManager.findFragmentById(R.id.ftc_container) == null) {
            cicerone.router.replaceScreen(MyBooksScreen())
        }
    }
}