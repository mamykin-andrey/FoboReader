package ru.mamykin.foboreader.app.ui

import android.os.Bundle
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import ru.mamykin.foboreader.core.data.SettingsStorage
import ru.mamykin.foboreader.core.extension.enableNightTheme
import ru.mamykin.foboreader.core.platform.Navigator
import ru.mamykin.foboreader.core.ui.BaseActivity
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.app.navigation.NavigatorImpl

class MainActivity : BaseActivity(R.layout.activity_main) {

    private val settingsStorage: SettingsStorage by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        initTheme()
        super.onCreate(savedInstanceState)
        initRouter()
    }

    fun openMyBooksScreen() {
        supportFragmentManager.findFragmentById(R.id.fr_main_nav_host)
                ?.childFragmentManager
                ?.fragments
                ?.firstOrNull()
                ?.let { it as? TabsFragment }
                ?.openMyBooksTab()
    }

    private fun initRouter() {
        loadKoinModules(module(override = true) {
            single<Navigator> { NavigatorImpl(this@MainActivity) }
        })
    }

    private fun initTheme() {
        setTheme(R.style.AppTheme)
        enableNightTheme(settingsStorage.isNightTheme)
    }

    fun showSnackbar(@StringRes messageResId: Int) {
        Snackbar.make(cl_main, getString(messageResId), Snackbar.LENGTH_SHORT)
    }
}