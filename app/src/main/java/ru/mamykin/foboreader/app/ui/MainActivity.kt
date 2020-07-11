package ru.mamykin.foboreader.app.ui

import android.os.Bundle
import org.koin.android.ext.android.inject
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.data.storage.SettingsStorage
import ru.mamykin.foboreader.core.extension.nightMode
import ru.mamykin.foboreader.core.platform.Navigator
import ru.mamykin.foboreader.core.ui.BaseActivity

class MainActivity : BaseActivity(R.layout.activity_main) {

    private val settingsStorage: SettingsStorage by inject()
    private val navigator by inject<Navigator>()

    override fun onCreate(savedInstanceState: Bundle?) {
        initTheme()
        super.onCreate(savedInstanceState)
        navigator.setActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        navigator.clearActivity()
    }

    private fun initTheme() {
        setTheme(R.style.AppTheme)
        nightMode = settingsStorage.isNightTheme
    }
}