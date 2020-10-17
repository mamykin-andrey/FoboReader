package ru.mamykin.foboreader.settings.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import reactivecircus.flowbinding.android.view.clicks
import ru.mamykin.foboreader.core.extension.appCompatActivity
import ru.mamykin.foboreader.core.extension.changeProgressEvents
import ru.mamykin.foboreader.core.extension.manualCheckedChanges
import ru.mamykin.foboreader.core.extension.nightMode
import ru.mamykin.foboreader.core.presentation.BaseFragment
import ru.mamykin.foboreader.core.presentation.viewBinding
import ru.mamykin.foboreader.settings.R
import ru.mamykin.foboreader.settings.databinding.FragmentSettingsBinding
import ru.mamykin.foboreader.settings.navigation.LocalSettingsNavigator

@FlowPreview
@ExperimentalCoroutinesApi
class SettingsFragment : BaseFragment<SettingsViewModel, ViewState, Effect>(R.layout.fragment_settings) {

    override val viewModel: SettingsViewModel by viewModel()

    private val binding by viewBinding { FragmentSettingsBinding.bind(requireView()) }
    private val navigator: LocalSettingsNavigator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigator.navController = findNavController()
    }

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
        binding.seekBright.changeProgressEvents()
            .onEach { viewModel.sendEvent(Event.BrightnessChanged(it)) }
            .launchIn(lifecycleScope)
        binding.swNightTheme.manualCheckedChanges()
            .onEach { viewModel.sendEvent(Event.NightThemeChanged(it)) }
            .launchIn(lifecycleScope)
        binding.swBrightAuto.manualCheckedChanges()
            .onEach { viewModel.sendEvent(Event.AutoBrightnessChanged(it)) }
            .launchIn(lifecycleScope)
        binding.btnTextSizeMinus.clicks()
            .onEach { viewModel.sendEvent(Event.DecreaseTextSizeClicked) }
            .launchIn(lifecycleScope)
        binding.btnTextSizePlus.clicks()
            .onEach { viewModel.sendEvent(Event.IncreaseTextSizeClicked) }
            .launchIn(lifecycleScope)
        binding.clTranslationColor.clicks()
            .onEach { viewModel.sendEvent(Event.SelectReadColorClicked) }
            .launchIn(lifecycleScope)
    }

    override fun showState(state: ViewState) {
        progressView.isVisible = state.isLoading
        state.settings?.let {
            showTheme(it.isNightTheme)
            showBrightness(it.isAutoBrightness, it.brightness)
            binding.swBrightAuto.isChecked = it.isAutoBrightness
            binding.tvTextSize.text = it.contentTextSize.toString()
        }
    }

    private fun showTheme(nightTheme: Boolean) {
        binding.swNightTheme.isChecked = nightTheme
        appCompatActivity.nightMode = nightTheme
    }

    private fun showBrightness(autoBrightness: Boolean, brightnessValue: Int) {
        binding.seekBright.isEnabled = !autoBrightness
        binding.seekBright.progress = brightnessValue
    }

    override fun onDestroy() {
        super.onDestroy()
        navigator.navController = null
    }
}