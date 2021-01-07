package ru.mamykin.foboreader.app.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import org.koin.android.ext.android.inject
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.app.navigation.AppNavigator

class MainActivity : AppCompatActivity() {

    private val nightThemeDelegate = NightThemeDelegate(this)
    private val appLanguageDelegate = AppLanguageDelegate(this)
    private val navigator by inject<AppNavigator>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTheme()
        setContentView(R.layout.activity_main)
        navigator.navController = findNavController(R.id.fr_main_nav_host)
    }

    override fun onDestroy() {
        super.onDestroy()
        navigator.navController = null
    }

    private fun initTheme() {
        setTheme(R.style.AppTheme)
        nightThemeDelegate.init()
        appLanguageDelegate.init()
    }
}