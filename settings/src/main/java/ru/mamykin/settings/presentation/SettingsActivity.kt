package ru.mamykin.settings.presentation

import android.content.Context
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_settings.*
import ru.mamykin.core.extension.setOnSeekBarChangeListener
import ru.mamykin.core.extension.startActivity
import ru.mamykin.core.ui.BaseActivity
import ru.mamykin.core.ui.UiUtils
import ru.mamykin.settings.R
import ru.mamykin.settings.data.SettingsStorage

class SettingsActivity : BaseActivity(R.layout.activity_settings) {

    companion object {

        fun start(context: Context) {
            context.startActivity<SettingsActivity>()
        }
    }

    private lateinit var settings: SettingsStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar(getString(R.string.settings), true)
        initViews()
        loadSettings()
    }

    private fun loadSettings() {
        switchNightTheme.isChecked = settings.isNightTheme
        switchBrightAuto.isChecked = !settings.isManualBrightness
        seekbarBright.isEnabled = settings.isManualBrightness
        seekbarBright.progress = settings.brightness
        tvTextSize.text = settings.readTextSize.toString()
    }

    private fun enableNightTheme(enable: Boolean) {
        settings.isNightTheme = enable
        UiUtils.enableNightMode(enable)
    }

    private fun enableAutoBrightness(enable: Boolean) {
        switchBrightAuto.isChecked = enable
        seekbarBright.isEnabled = !enable
    }

    private fun initViews() {
        seekbarBright.setOnSeekBarChangeListener { settings.brightness = it }
        switchNightTheme.setOnCheckedChangeListener { _, c -> enableNightTheme(c) }
        switchBrightAuto.setOnCheckedChangeListener { _, c -> enableAutoBrightness(c) }
    }
}