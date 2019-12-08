package ru.mamykin.settings.presentation

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.android.ext.android.inject
import ru.mamykin.core.data.SettingsStorage
import ru.mamykin.core.extension.setOnSeekBarChangeListener
import ru.mamykin.core.ui.BaseFragment
import ru.mamykin.core.ui.UiUtils
import ru.mamykin.settings.R

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    companion object {

        fun newInstance() = SettingsFragment()
    }

    private val settings: SettingsStorage by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initViews()
        loadSettings()
    }

    private fun initToolbar() {
        toolbar!!.setTitle(R.string.settings_screen)
    }

    private fun initViews() {
        seekbarBright.setOnSeekBarChangeListener { settings.brightness = it }
        switchNightTheme.setOnCheckedChangeListener { _, c -> enableNightTheme(c) }
        switchBrightAuto.setOnCheckedChangeListener { _, c -> enableAutoBrightness(c) }
        btnTextSizeMinus.setOnClickListener {
            settings.readTextSize--
            tvTextSize.text = settings.readTextSize.toString()
        }
        btnTextSizePlus.setOnClickListener {
            settings.readTextSize++
            tvTextSize.text = settings.readTextSize.toString()
        }
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
}