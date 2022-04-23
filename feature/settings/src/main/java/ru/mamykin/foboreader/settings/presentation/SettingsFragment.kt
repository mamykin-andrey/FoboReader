package ru.mamykin.foboreader.settings.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.mamykin.foboreader.core.di.ComponentHolder
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.extension.collectWithRepeatOnStarted
import ru.mamykin.foboreader.core.extension.commonApi
import ru.mamykin.foboreader.core.presentation.BaseFragment
import ru.mamykin.foboreader.core.presentation.autoCleanedValue
import ru.mamykin.foboreader.settings.R
import ru.mamykin.foboreader.settings.databinding.FragmentSettingsBinding
import ru.mamykin.foboreader.settings.di.DaggerSettingsComponent
import ru.mamykin.foboreader.settings.presentation.list.SettingsListAdapter
import javax.inject.Inject

class SettingsFragment : BaseFragment(R.layout.fragment_settings), DialogDismissedListener {

    companion object {
        fun newInstance(): Fragment = SettingsFragment()
    }

    override val featureName: String = "settings"

    @Inject
    internal lateinit var feature: SettingsFeature

    private val binding by autoCleanedValue { FragmentSettingsBinding.bind(requireView()) }
    private val adapter by lazy { SettingsListAdapter(feature::sendIntent) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDi()
    }

    private fun initDi() {
        ComponentHolder.getOrCreateComponent(featureName) {
            DaggerSettingsComponent.factory().create(
                commonApi(),
                apiHolder().settingsApi(),
                apiHolder().navigationApi(),
            )
        }.inject(this)
    }

    override fun onCleared() {
        feature.onCleared()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initSettingsList()
        initFeature()
    }

    override fun onDestroyView() {
        binding.rvSettings.adapter = null
        super.onDestroyView()
    }

    override fun onDismiss() {
        feature.sendIntent(SettingsFeature.Intent.LoadSettings)
    }

    private fun initToolbar() {
        binding.toolbar.apply {
            setTitle(R.string.settings_title)
            navigationIcon = null
        }
    }

    private fun initSettingsList() {
        binding.rvSettings.adapter = adapter
    }

    private fun initFeature() {
        feature.stateFlow.collectWithRepeatOnStarted(viewLifecycleOwner, ::showState)
        feature.effectFlow.collectWithRepeatOnStarted(viewLifecycleOwner, ::takeEffect)
    }

    private fun showState(state: SettingsFeature.State) {
        state.settings?.let { adapter.submitList(it) }
    }

    private fun takeEffect(effect: SettingsFeature.Effect) {
        when (effect) {
            is SettingsFeature.Effect.SelectReadColor -> {
                ChangeTranslationColorDialogFragment.newInstance().show(
                    childFragmentManager,
                    ChangeTranslationColorDialogFragment.TAG
                )
            }
            is SettingsFeature.Effect.SelectAppLanguage -> {
                ChangeLanguageDialogFragment.newInstance().show(
                    childFragmentManager,
                    ChangeLanguageDialogFragment.TAG
                )
            }
        }
    }
}