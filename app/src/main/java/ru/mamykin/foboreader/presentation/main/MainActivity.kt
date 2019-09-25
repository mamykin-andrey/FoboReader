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
import ru.mamykin.foboreader.core.ui.BaseActivity
import ru.mamykin.foboreader.presentation.booksstore.BooksStoreFragment
import ru.mamykin.foboreader.presentation.devicebooks.DeviceBooksFragment
import ru.mamykin.foboreader.presentation.mybooks.MyBooksFragment
import ru.mamykin.foboreader.presentation.settings.SettingsActivity

/**
 * Основная страница, включает в себя страницу с книгами, файлами на устройстве, файлами Dropbox, магазином
 */
class MainActivity : BaseActivity() {

    companion object {

        private const val MY_BOOKS_FRAGMENT_POS = 0
        private const val DEVICE_BOOKS_FRAGMENT_POS = 1
        private const val BOOKS_STORE_FRAGMENT_POS = 3

        fun start(context: Context) = context.startActivity(
                Intent(context, MainActivity::class.java)
        )
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
        viewPager.adapter = MainViewPagerAdapter(supportFragmentManager).apply {
            addFragment(MyBooksFragment.newInstance(), getString(R.string.my_books))
            addFragment(DeviceBooksFragment.newInstance(), getString(R.string.device))
            addFragment(BooksStoreFragment.newInstance(), getString(R.string.store))
        }
    }

    private fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawer(GravityCompat.START)

        when (item.itemId) {
            R.id.menu_my_books -> viewpager.currentItem = MY_BOOKS_FRAGMENT_POS
            R.id.menu_device -> viewpager.currentItem = DEVICE_BOOKS_FRAGMENT_POS
            R.id.menu_books_store -> viewpager.currentItem = BOOKS_STORE_FRAGMENT_POS
            R.id.menu_settings -> SettingsActivity.start(this)
            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }

    private fun setupDrawerLayout() {
        navigationDrawer.setNavigationItemSelectedListener { onNavigationItemSelected(it) }
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name).apply {
            isDrawerIndicatorEnabled = true
        }.also {
            drawerLayout.addDrawerListener(it)
        }
    }
}