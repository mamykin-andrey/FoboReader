package ru.mamykin.foboreader.settings.presentation

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import reactivecircus.flowbinding.android.view.clicks
import ru.mamykin.foboreader.core.extension.appCompatActivity
import ru.mamykin.foboreader.core.extension.changeProgressEvents
import ru.mamykin.foboreader.core.extension.manualCheckedChanges
import ru.mamykin.foboreader.core.extension.nightMode
import ru.mamykin.foboreader.core.ui.BaseFragment2
import ru.mamykin.foboreader.settings.R

@FlowPreview
@ExperimentalCoroutinesApi
class SettingsFragment : BaseFragment2<SettingsViewModel, ViewState, SettingsEffect>(
    R.layout.fragment_settings
) {
    override val viewModel: SettingsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initViews()
    }

    private fun initToolbar() = toolbar!!.apply {
        setTitle(R.string.settings_screen)
    }

    private fun initViews() {
        seekbarBright.changeProgressEvents()
            .onEach { viewModel.sendEvent(SettingsEvent.BrightnessChanged(it)) }
            .launchIn(lifecycleScope)
        switchNightTheme.manualCheckedChanges()
            .onEach { viewModel.sendEvent(SettingsEvent.NightThemeChanged(it)) }
            .launchIn(lifecycleScope)
        switchBrightAuto.manualCheckedChanges()
            .onEach { viewModel.sendEvent(SettingsEvent.AutoBrightnessChanged(it)) }
            .launchIn(lifecycleScope)
        btnTextSizeMinus.clicks()
            .onEach { viewModel.sendEvent(SettingsEvent.DecreaseTextSizeClicked) }
            .launchIn(lifecycleScope)
        btnTextSizePlus.clicks()
            .onEach { viewModel.sendEvent(SettingsEvent.IncreaseTextSizeClicked) }
            .launchIn(lifecycleScope)
        clTranslationColor.clicks()
            .onEach { viewModel.sendEvent(SettingsEvent.SelectReadColorClicked) }
            .launchIn(lifecycleScope)
    }

    override fun showState(state: ViewState) {
        showTheme(state.isNightTheme)
        showBrightness(state.isAutoBrightness, state.brightness)
        switchBrightAuto.isChecked = state.isAutoBrightness
        tvTextSize.text = state.contentTextSize.toString()
    }

    private fun showBrightness(autoBrightness: Boolean, brightnessValue: Int) {
        seekbarBright.isEnabled = !autoBrightness
        seekbarBright.progress = brightnessValue
    }

    private fun showTheme(nightTheme: Boolean) {
        switchNightTheme.isChecked = nightTheme
        appCompatActivity.nightMode = nightTheme
    }

    override fun takeEffect(effect: SettingsEffect) {
        when (effect) {
            is SettingsEffect.OpenSelectReadColorScreen ->
                ColorPickerFragment().show(activity!!.supportFragmentManager, null)
        }
    }
}