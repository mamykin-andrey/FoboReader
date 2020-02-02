package ru.mamykin.foboreader.ui

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.root_activity.*
import org.koin.android.ext.android.inject
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import ru.mamykin.core.data.SettingsStorage
import ru.mamykin.core.platform.Navigator
import ru.mamykin.core.ui.BaseActivity
import ru.mamykin.core.ui.UiUtils
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.navigation.KeepStateNavigator
import ru.mamykin.foboreader.navigation.MainNavigator

class RootActivity : BaseActivity(R.layout.root_activity) {

    private val settingsStorage: SettingsStorage by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        initRouter()
        initTheme()
        initBottomNavigationView()
    }

    private fun initRouter() {
        loadKoinModules(module(override = true) {
            single<Navigator> {
                MainNavigator(this@RootActivity)
            }
        })
    }

    private fun initBottomNavigationView() {
        val navController = findNavController(R.id.fr_main_nav_host)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fr_main_nav_host)
        val navigator = KeepStateNavigator(
                this,
                navHostFragment!!.childFragmentManager,
                R.id.fr_main_nav_host
        )
        navController.navigatorProvider.addNavigator(navigator)
        navController.setGraph(R.navigation.main)
        bnv_main.setupWithNavController(navController)
    }

    private fun initTheme() {
        val nightModeEnabled = settingsStorage.isNightTheme
        UiUtils.enableNightMode(nightModeEnabled)
    }
}