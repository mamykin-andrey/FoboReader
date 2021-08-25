package ru.mamykin.foboreader.app.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import org.koin.android.ext.android.inject
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.navigation.screen.MainScreen

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val nightThemeDelegate = NightThemeDelegate(this)
    private val appLanguageDelegate = AppLanguageDelegate(this)
    private val navigatorHolder: NavigatorHolder by inject()
    private val router: Router by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTheme()
        navigatorHolder.setNavigator(AppNavigator(this, R.id.fcv_root))
        router.navigateTo(MainScreen())
    }

    override fun onDestroy() {
        super.onDestroy()
        navigatorHolder.removeNavigator()
    }

    private fun initTheme() {
        setTheme(R.style.AppTheme)
        nightThemeDelegate.init()
        appLanguageDelegate.init()
    }
}