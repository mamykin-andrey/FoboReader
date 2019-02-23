package ru.mamykin.foboreader.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.presentation.booksstore.BooksStoreFragment
import ru.mamykin.foboreader.presentation.devicebooks.DeviceBooksFragment
import ru.mamykin.foboreader.presentation.dropboxbooks.DropboxBooksFragment
import ru.mamykin.foboreader.core.ui.BaseActivity
import ru.mamykin.foboreader.presentation.mybooks.MyBooksFragment
import ru.mamykin.foboreader.presentation.settings.SettingsActivity

/**
 * Основная страница, включает в себя страницу с книгами, файлами на устройстве, файлами Dropbox, магазином
 */
class MainActivity : BaseActivity() {

    companion object {

        private const val MY_BOOKS_FRAGMENT_POS = 0
        private const val DEVICE_BOOKS_FRAGMENT_POS = 1
        private const val DROPBOX_BOOKS_FRAGMENT_POS = 2
        private const val BOOKS_STORE_FRAGMENT_POS = 3

        fun getHomeIntent(): Intent {
            val homeIntent = Intent(Intent.ACTION_MAIN)
            homeIntent.addCategory(Intent.CATEGORY_HOME)
            homeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            return homeIntent
        }

        fun getStartIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    private lateinit var toggle: ActionBarDrawerToggle

    override val layout: Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar(getString(R.string.main_tab), true)
        setupViewPager(viewpager)
        tabLayout.setupWithViewPager(viewpager)
        setupDrawerLayout()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = MainViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(MyBooksFragment.newInstance(), getString(R.string.my_books))
        adapter.addFragment(DeviceBooksFragment.newInstance(), getString(R.string.device))
        adapter.addFragment(DropboxBooksFragment.newInstance(), getString(R.string.dropbox))
        adapter.addFragment(BooksStoreFragment.newInstance(), getString(R.string.store))
        viewPager.adapter = adapter
    }

    private fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawer(GravityCompat.START)

        when (item.itemId) {
            R.id.menu_my_books -> viewpager.currentItem = MY_BOOKS_FRAGMENT_POS
            R.id.menu_device -> viewpager.currentItem = DEVICE_BOOKS_FRAGMENT_POS
            R.id.menu_dropbox -> viewpager.currentItem = DROPBOX_BOOKS_FRAGMENT_POS
            R.id.menu_books_store -> viewpager.currentItem = BOOKS_STORE_FRAGMENT_POS
            R.id.menu_settings -> startActivity(SettingsActivity.getStartIntent(this))
            R.id.menu_exit -> startActivity(getHomeIntent())
            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }

    private fun setupDrawerLayout() {
        navigationDrawer.setNavigationItemSelectedListener({ this.onNavigationItemSelected(it) })
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name)
        toggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toggle)
    }
}