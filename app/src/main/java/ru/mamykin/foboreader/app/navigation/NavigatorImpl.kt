package ru.mamykin.foboreader.app.navigation

import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.app.ui.TabsFragment
import ru.mamykin.foboreader.app.ui.TabsFragmentDirections
import ru.mamykin.foboreader.core.platform.Navigator

class NavigatorImpl : Navigator {

    private var activity: AppCompatActivity? = null
    private var navController: NavController? = null

    override fun setActivity(activity: AppCompatActivity) {
        this.activity = activity
        this.navController = activity.findNavController(R.id.fr_main_nav_host)
    }

    override fun clearActivity() {
        activity = null
        navController = null
    }

    override fun openMyBooksScreen() {
        activity?.supportFragmentManager
            ?.findFragmentById(R.id.fr_main_nav_host)
            ?.childFragmentManager
            ?.fragments
            ?.firstOrNull()
            ?.let { it as? TabsFragment }
            ?.openMyBooksTab()
    }

    override fun openTabsScreen() {
        navController?.navigate(R.id.tabsFragment)
    }

    override fun openBook(id: Long) {
        val action = TabsFragmentDirections.tabsToReadBook()
            .setBookId(id)
        navController?.navigate(action)
    }

    override fun openBookDetails(id: Long, sharedImage: ImageView) {
        val action = TabsFragmentDirections.tabsToBookDetails()
            .setBookId(id)
        val extras = FragmentNavigatorExtras(sharedImage to id.toString())
        navController?.navigate(action, extras)
    }
}