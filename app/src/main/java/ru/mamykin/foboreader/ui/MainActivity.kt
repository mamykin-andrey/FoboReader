package ru.mamykin.foboreader.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import ru.mamykin.core.extension.startActivity
import ru.mamykin.core.ui.BaseActivity
import ru.mamykin.foboreader.R
import ru.mamykin.my_books.presentation.my_books.MyBooksFragment
import ru.mamykin.settings.presentation.SettingsFragment
import ru.mamykin.store.presentation.BooksStoreFragment

class MainActivity : BaseActivity(R.layout.activity_main) {

    companion object {

        fun start(context: Context) {
            context.startActivity<MainActivity>()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBottomNavigationView()
    }

    private fun initBottomNavigationView() {
        bnvMain.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_my_books -> openScreen(MyBooksFragment.Companion::newInstance)
                R.id.menu_books_store -> openScreen(BooksStoreFragment.Companion::newInstance)
                R.id.menu_settings -> openScreen(SettingsFragment.Companion::newInstance)
            }
            return@setOnNavigationItemSelectedListener true
        }
        openScreen(MyBooksFragment.Companion::newInstance)
    }

    private inline fun <reified T : Fragment> openScreen(newInstance: () -> T) {
        val tag = T::class.java.name
        val fragment = supportFragmentManager.findFragmentByTag(tag) ?: newInstance()
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frContent, fragment, tag)
                .commit()
    }
}