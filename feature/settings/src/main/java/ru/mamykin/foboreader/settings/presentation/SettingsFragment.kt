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
import ru.mamykin.foboreader.core.extension.*
import ru.mamykin.foboreader.core.ui.BaseFragment
import ru.mamykin.foboreader.settings.R

@FlowPreview
@ExperimentalCoroutinesApi
class SettingsFragment : BaseFragment<SettingsViewModel, ViewState, Effect>(
    R.layout.fragment_settings
) {
    override val viewModel: SettingsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initViews()
    }

    private fun initToolbar() = toolbar.apply {
        setTitle(R.string.settings_title)
        navigationIcon = null
    }

    private fun initViews() {
        seekbarBright.changeProgressEvents()
            .onEach { viewModel.sendEvent(Event.BrightnessChanged(it)) }
            .launchIn(lifecycleScope)
        switchNightTheme.manualCheckedChanges()
            .onEach { viewModel.sendEvent(Event.NightThemeChanged(it)) }
            .launchIn(lifecycleScope)
        switchBrightAuto.manualCheckedChanges()
            .onEach { viewModel.sendEvent(Event.AutoBrightnessChanged(it)) }
            .launchIn(lifecycleScope)
        btnTextSizeMinus.clicks()
            .onEach { viewModel.sendEvent(Event.DecreaseTextSizeClicked) }
            .launchIn(lifecycleScope)
        btnTextSizePlus.clicks()
            .onEach { viewModel.sendEvent(Event.IncreaseTextSizeClicked) }
            .launchIn(lifecycleScope)
        clTranslationColor.clicks()
            .onEach { viewModel.sendEvent(Event.SelectReadColorClicked) }
            .launchIn(lifecycleScope)
    }

    override fun showState(state: ViewState) {
        progressView.isVisible = state.isLoading
        state.settings?.let {
            showTheme(it.isNightTheme)
            showBrightness(it.isAutoBrightness, it.brightness)
            switchBrightAuto.isChecked = it.isAutoBrightness
            tvTextSize.text = it.contentTextSize.toString()
        }
    }

    private fun showTheme(nightTheme: Boolean) {
        switchNightTheme.isChecked = nightTheme
        appCompatActivity.nightMode = nightTheme
    }

    private fun showBrightness(autoBrightness: Boolean, brightnessValue: Int) {
        seekbarBright.isEnabled = !autoBrightness
        seekbarBright.progress = brightnessValue
    }

    override fun takeEffect(effect: Effect) {
        when (effect) {
            is Effect.OpenSelectReadColorScreen ->
                ColorPickerFragment().show(activity!!.supportFragmentManager, null)
        }
    }
}