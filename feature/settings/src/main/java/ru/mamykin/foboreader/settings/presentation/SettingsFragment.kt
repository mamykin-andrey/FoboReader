package ru.mamykin.foboreader.settings.presentation

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import reactivecircus.flowbinding.android.view.clicks
import reactivecircus.flowbinding.android.widget.SeekBarChangeEvent
import reactivecircus.flowbinding.android.widget.changeEvents
import reactivecircus.flowbinding.android.widget.checkedChanges
import ru.mamykin.foboreader.core.extension.appCompatActivity
import ru.mamykin.foboreader.core.extension.enableNightTheme
import ru.mamykin.foboreader.core.ui.BaseFragment
import ru.mamykin.foboreader.settings.R

@ExperimentalCoroutinesApi
class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private val viewModel: SettingsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initViews()
        initViewModel()
        viewModel.loadSettings()
    }

    private fun initToolbar() = toolbar!!.apply {
        setTitle(R.string.settings_screen)
    }

    private fun initViews() {
        seekbarBright.changeEvents()
                .filter { it is SeekBarChangeEvent.ProgressChanged && it.fromUser }
                .onEach { viewModel.changeBrightness(it.view.progress) }
                .launchIn(lifecycleScope)
        switchNightTheme.checkedChanges()
                .onEach { viewModel.enableNightTheme(it) }
                .launchIn(lifecycleScope)
        switchBrightAuto.checkedChanges()
                .onEach { viewModel.enableAutoBrightness(it) }
                .launchIn(lifecycleScope)
        btnTextSizeMinus.clicks()
                .onEach { viewModel.decreaseTextSize() }
                .launchIn(lifecycleScope)
        btnTextSizePlus.clicks()
                .onEach { viewModel.increaseTextSize() }
                .launchIn(lifecycleScope)
    }

    private fun initViewModel() {
        viewModel.stateLiveData.observe { state ->
            showTheme(state.nightTheme)
            showBrightness(state.autoBrightness, state.brightnessValue)
            switchBrightAuto.isChecked = state.autoBrightness
            tvTextSize.text = "${state.textSize}"
        }
    }

    private fun showBrightness(autoBrightness: Boolean, brightnessValue: Int) {
        seekbarBright.isEnabled = !autoBrightness
        seekbarBright.progress = brightnessValue
    }

    private fun showTheme(nightTheme: Boolean) {
        switchNightTheme.isChecked = nightTheme
        appCompatActivity.enableNightTheme(nightTheme)
    }
}