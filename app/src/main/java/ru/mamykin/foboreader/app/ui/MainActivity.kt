package ru.mamykin.foboreader.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import org.koin.android.ext.android.inject
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.data.storage.SettingsStorage
import ru.mamykin.foboreader.core.extension.nightMode
import ru.mamykin.foboreader.core.platform.NavControllerHolder

class MainActivity : AppCompatActivity() {

    private val settingsStorage: SettingsStorage by inject()
    private val navigator by inject<NavControllerHolder>()

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
        nightMode = settingsStorage.isNightTheme
    }
}