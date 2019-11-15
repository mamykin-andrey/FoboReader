package ru.mamykin.foboreader.presentation.main

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.extension.startActivity
import ru.mamykin.foboreader.core.ui.BaseActivity
import ru.mamykin.foboreader.presentation.booksstore.BooksStoreFragment
import ru.mamykin.foboreader.presentation.devicebooks.DeviceBooksFragment
import ru.mamykin.foboreader.presentation.mybooks.MyBooksFragment

class MainActivity : BaseActivity(R.layout.activity_main) {

    companion object {

        fun start(context: Context) {
            context.startActivity<MainActivity>()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar(getString(R.string.app_name), false)
        initBottomNavigationView()
    }

    private fun initBottomNavigationView() {
        bnvMain.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_my_books -> openScreen(MyBooksFragment.newInstance())
                R.id.menu_device -> openScreen(DeviceBooksFragment.newInstance())
                R.id.menu_books_store -> openScreen(BooksStoreFragment.newInstance())
                R.id.menu_settings -> openScreen(BooksStoreFragment.newInstance())
            }
            true
        }
    }

    private fun openScreen(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frContent, fragment)
                .commit()
    }
}