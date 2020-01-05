package ru.mamykin.settings.presentation

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mamykin.core.extension.setOnSeekBarChangeListener
import ru.mamykin.core.ui.BaseFragment
import ru.mamykin.settings.R

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    companion object {

        fun newInstance() = SettingsFragment()
    }

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
            viewModel.onEvent(SettingsViewModel.Event.BrightnessChanged(it))
        }
        switchNightTheme.setOnCheckedChangeListener { _, c ->
            viewModel.onEvent(SettingsViewModel.Event.NightThemeEnabled(c))
        }
        switchBrightAuto.setOnCheckedChangeListener { _, c ->
            viewModel.onEvent(SettingsViewModel.Event.BrightAutoEnabled(c))
        }
        btnTextSizeMinus.setOnClickListener {
            viewModel.onEvent(SettingsViewModel.Event.TextSizeDecreased)
        }
        btnTextSizePlus.setOnClickListener {
            viewModel.onEvent(SettingsViewModel.Event.TextSizeIncreased)
        }
    }

    private fun initViewModel() {
        viewModel.stateLiveData.observe(viewLifecycleOwner, Observer { state ->
            switchNightTheme.isChecked = state.nightTheme
            seekbarBright.isEnabled = state.manualBrightness
            switchBrightAuto.isChecked = !state.manualBrightness
            seekbarBright.progress = state.brightness
            tvTextSize.text = state.textSize
        })
        viewModel.loadSettings()
    }
}