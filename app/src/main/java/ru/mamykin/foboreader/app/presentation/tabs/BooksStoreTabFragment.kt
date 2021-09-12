package ru.mamykin.foboreader.app.presentation.tabs

import android.os.Bundle
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.app.di.DaggerMainComponent
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.navigation.ScreenProvider
import ru.mamykin.foboreader.core.navigation.TabContainerFragment
import javax.inject.Inject

class BooksStoreTabFragment : TabContainerFragment(Tab.BooksStore.tag) {

    @Inject
    lateinit var screenProvider: ScreenProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerMainComponent.factory().create(apiHolder().navigationApi()).inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (childFragmentManager.findFragmentById(R.id.ftc_container) == null) {
            cicerone.router.replaceScreen(screenProvider.booksStoreScreen())
        }
    }
}