package ru.mamykin.foboreader.app.presentation

import android.os.Bundle
import org.koin.android.ext.android.inject
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.navigation.ScreenProvider
import ru.mamykin.foboreader.core.navigation.TabContainerFragment

class BooksStoreTabFragment : TabContainerFragment(Tab.BooksStoreTab.tag) {

    private val screenProvider: ScreenProvider by inject()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (childFragmentManager.findFragmentById(R.id.ftc_container) == null) {
            cicerone.router.replaceScreen(screenProvider.booksStoreScreen())
        }
    }
}