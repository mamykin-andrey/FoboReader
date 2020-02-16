package ru.mamykin.foboreader.settings.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mamykin.foboreader.core.extension.enableNightTheme
import ru.mamykin.foboreader.core.extension.setOnSeekBarChangeListener
import ru.mamykin.foboreader.core.ui.BaseFragment
import ru.mamykin.foboreader.settings.R

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private val viewModel: SettingsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initViews()
        initViewModel()
    }

    private fun initToolbar() {
        toolbar!!.setTitle(R.string.settings_screen)
    }

    private fun initViews() {
        seekbarBright.setOnSeekBarChangeListener {
            viewModel.changeBrightness(it)
        }
        switchNightTheme.setOnCheckedChangeListener { _, c ->
            viewModel.changeTheme(c)
        }
        switchBrightAuto.setOnCheckedChangeListener { _, c ->
            viewModel.switchAutoBrightness(c)
        }
        btnTextSizeMinus.setOnClickListener {
            viewModel.decreaseTextSize()
        }
        btnTextSizePlus.setOnClickListener {
            viewModel.increaseTextSize()
        }
    }

    private fun initViewModel() {
        viewModel.stateLiveData.observe(viewLifecycleOwner, Observer { state ->
            showTheme(state.nightTheme)
            seekbarBright.isEnabled = state.manualBrightness
            switchBrightAuto.isChecked = !state.manualBrightness
            seekbarBright.progress = state.brightness
            tvTextSize.text = "${state.textSize}"
        })
        viewModel.loadSettings()
    }

    private fun showTheme(nightTheme: Boolean) {
        switchNightTheme.isChecked = nightTheme
        (activity as AppCompatActivity).enableNightTheme(nightTheme)
    }
}