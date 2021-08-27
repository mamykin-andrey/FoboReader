package ru.mamykin.foboreader.settings.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.afollestad.recyclical.datasource.dataSourceTypedOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mamykin.foboreader.core.presentation.NewBaseFragment
import ru.mamykin.foboreader.core.presentation.viewBinding
import ru.mamykin.foboreader.settings.R
import ru.mamykin.foboreader.settings.databinding.*
import ru.mamykin.foboreader.settings.domain.model.SettingsItem
import ru.mamykin.foboreader.settings.presentation.list.*

class SettingsFragment : NewBaseFragment(R.layout.fragment_settings) {

    private val viewModel: SettingsViewModel by viewModel()
    private val binding by viewBinding { FragmentSettingsBinding.bind(requireView()) }
    private val settingsSource = dataSourceTypedOf<SettingsItem>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initSettingsList()
        initViewModel()
    }

    private fun initToolbar() = toolbar!!.apply {
        setTitle(R.string.settings_title)
        navigationIcon = null
    }

    private fun initSettingsList() {
        binding.rvSettings.setup {
            withDataSource(settingsSource)
            withItem<SettingsItem.NightTheme, NightThemeHolder>(R.layout.item_night_theme) {
                onBind({
                    NightThemeHolder(
                        ItemNightThemeBinding.bind(it),
                        viewModel::sendEvent,
                        viewLifecycleOwner.lifecycleScope
                    )
                }) { _, item -> bind(item) }
            }
            withItem<SettingsItem.Brightness, BrightnessHolder>(R.layout.item_background_brightness) {
                onBind({
                    BrightnessHolder(
                        ItemBackgroundBrightnessBinding.bind(it),
                        viewModel::sendEvent,
                        viewLifecycleOwner.lifecycleScope
                    )
                }) { _, item -> bind(item) }
            }
            withItem<SettingsItem.ReadTextSize, TextSizeHolder>(R.layout.item_text_size) {
                onBind({
                    TextSizeHolder(
                        ItemTextSizeBinding.bind(it),
                        viewModel::sendEvent,
                        viewLifecycleOwner.lifecycleScope
                    )
                }) { _, item -> bind(item) }
            }
            withItem<SettingsItem.TranslationColor, TranslationColorHolder>(R.layout.item_translation_color) {
                onBind({
                    TranslationColorHolder(
                        ItemTranslationColorBinding.bind(it),
                        viewModel::sendEvent,
                        viewLifecycleOwner.lifecycleScope
                    )
                }) { _, item -> bind(item) }
            }
            withItem<SettingsItem.AppLanguage, AppLanguageHolder>(R.layout.item_app_language) {
                onBind({
                    AppLanguageHolder(
                        ItemAppLanguageBinding.bind(it),
                        viewModel::sendEvent,
                        viewLifecycleOwner.lifecycleScope
                    )
                }) { _, item -> bind(item) }
            }
            withItem<SettingsItem.UseVibration, UseVibrationHolder>(R.layout.item_use_vibration) {
                onBind({
                    UseVibrationHolder(
                        ItemUseVibrationBinding.bind(it),
                        viewModel::sendEvent,
                        viewLifecycleOwner.lifecycleScope
                    )
                }) { _, item -> bind(item) }
            }
        }
    }

    private fun initViewModel() {
        viewModel.stateLiveData.observe(viewLifecycleOwner, ::showState)
    }

    private fun showState(state: ViewState) {
        progressView.isVisible = state.isLoading
        binding.rvSettings.post {
            state.settings?.let(settingsSource::set)
        }
    }
}