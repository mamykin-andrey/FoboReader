package ru.mamykin.foreignbooksreader.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem

import com.arellomobile.mvp.presenter.InjectPresenter

import butterknife.BindView
import ru.mamykin.foreignbooksreader.R
import ru.mamykin.foreignbooksreader.common.UiUtils
import ru.mamykin.foreignbooksreader.presenters.MainPresenter
import ru.mamykin.foreignbooksreader.ui.adapters.MainViewPagerAdapter
import ru.mamykin.foreignbooksreader.ui.fragments.BooksStoreFragment
import ru.mamykin.foreignbooksreader.ui.fragments.DeviceBooksFragment
import ru.mamykin.foreignbooksreader.ui.fragments.DropboxBooksFragment
import ru.mamykin.foreignbooksreader.ui.fragments.MyBooksFragment
import ru.mamykin.foreignbooksreader.views.MainView

/**
 * Основная страница, включает в себя страницу с книгами, файлами на устройстве, файлами Dropbox, магазином
 */
class MainActivity(override val layout: Int = R.layout.activity_main) : BaseActivity(), MainView {

    @BindView(R.id.drawerLayout)
    protected var drawerLayout: DrawerLayout? = null
    @BindView(R.id.viewpager)
    protected var viewPager: ViewPager? = null
    @BindView(R.id.tabLayout)
    protected var tabLayout: TabLayout? = null
    @BindView(R.id.navigationDrawer)
    protected var navigationView: NavigationView? = null

    @InjectPresenter
    internal var presenter: MainPresenter? = null

    private var toggle: ActionBarDrawerToggle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar(getString(R.string.main_tab), true)
        setupViewPager(viewPager!!, savedInstanceState)
        tabLayout!!.setupWithViewPager(viewPager)
        setupDrawerLayout()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle!!.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            drawerLayout!!.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            drawerLayout!!.closeDrawer(GravityCompat.START)
            return
        }
        super.onBackPressed()
    }

    private fun setupViewPager(viewPager: ViewPager, savedInstanceState: Bundle?) {
        viewPager.offscreenPageLimit = TABS_COUNT
        val adapter = MainViewPagerAdapter(supportFragmentManager)
        if (savedInstanceState != null) {
            adapter.addFragment(supportFragmentManager.getFragment(
                    savedInstanceState, TAG_MY_BOOKS_FRAGMENT), getString(R.string.my_books))
            adapter.addFragment(supportFragmentManager.getFragment(
                    savedInstanceState, TAG_STORE_FRAGMENT), getString(R.string.store))
            adapter.addFragment(supportFragmentManager.getFragment(
                    savedInstanceState, TAG_DEVICE_BOOKS_FRAGMENT), getString(R.string.device))
            adapter.addFragment(supportFragmentManager.getFragment(
                    savedInstanceState, TAG_DROPBOX_BOOKS_FRAGMENT), getString(R.string.dropbox))
        } else {
            adapter.addFragment(MyBooksFragment.newInstance(), getString(R.string.my_books))
            adapter.addFragment(DeviceBooksFragment.newInstance(null), getString(R.string.device))
            adapter.addFragment(DropboxBooksFragment.newInstance(null), getString(R.string.dropbox))
            adapter.addFragment(BooksStoreFragment.newInstance(), getString(R.string.store))
        }
        viewPager.adapter = adapter
    }

    fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout!!.closeDrawer(GravityCompat.START)
        when (item.itemId) {
            R.id.menu_my_books -> {
                viewPager!!.currentItem = MY_BOOKS_FRAGMENT_POS
                return true
            }
            R.id.menu_device -> {
                viewPager!!.currentItem = DEVICE_BOOKS_FRAGMENT_POS
                return true
            }
            R.id.menu_dropbox -> {
                viewPager!!.currentItem = DROPBOX_BOOKS_FRAGMENT_POS
                return true
            }
            R.id.menu_settings -> {
                startActivity(SettingsActivity.getStartIntent(this))
                return true
            }
            R.id.menu_about -> {
                startActivity(AboutActivity.getStartIntent(this))
                return true
            }
            R.id.menu_exit -> {
                startActivity(MainActivity.homeIntent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun restartActivity() {
        UiUtils.restartActivity(this)
    }

    protected fun setupDrawerLayout() {
        navigationView!!.setNavigationItemSelectedListener({ this.onNavigationItemSelected(it) })
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name)
        toggle!!.isDrawerIndicatorEnabled = true
        drawerLayout!!.addDrawerListener(toggle!!)
    }

    companion object {
        private val TAG_MY_BOOKS_FRAGMENT = "tag_my_books_fragment"
        private val TAG_STORE_FRAGMENT = "tag_store_fragment"
        private val TAG_DEVICE_BOOKS_FRAGMENT = "tag_device_books_fragment"
        private val TAG_DROPBOX_BOOKS_FRAGMENT = "tag_dropbox_books_fragment"
        private val MY_BOOKS_FRAGMENT_POS = 0
        private val DEVICE_BOOKS_FRAGMENT_POS = 1
        private val DROPBOX_BOOKS_FRAGMENT_POS = 2
        private val TABS_COUNT = 4

        val homeIntent: Intent
            get() {
                val homeIntent = Intent(Intent.ACTION_MAIN)
                homeIntent.addCategory(Intent.CATEGORY_HOME)
                homeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                return homeIntent
            }

        fun getStartIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}