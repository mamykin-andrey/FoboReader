package ru.mamykin.foboreader.presentation.settings

import com.arellomobile.mvp.InjectViewState
import ru.mamykin.foboreader.core.platform.ResourcesManager
import ru.mamykin.foboreader.core.platform.Schedulers
import ru.mamykin.foboreader.core.ui.BasePresenter
import ru.mamykin.foboreader.domain.settings.SettingsInteractor
import javax.inject.Inject

@InjectViewState
class SettingsPresenter @Inject constructor(
        private val interactor: SettingsInteractor,
        override val resourcesManager: ResourcesManager,
        override val schedulers: Schedulers
) : BasePresenter<SettingsView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        showSettings()
    }

    fun onNightThemeEnabled(isChecked: Boolean) {
        interactor.enableNightTheme(isChecked)
    }

    fun onAutoBrightnessEnabled(isChecked: Boolean) {
        interactor.enableAutoBrightness(isChecked)
    }

    fun onBrightnessProgressChanged(progress: Int) {
        interactor.changeBrightness(progress)
    }

    private fun showSettings() = with(viewState) {
        showNightThemeEnabled(interactor.isNightThemeEnabled())
        showAutoBrightnessEnabled(!interactor.isManualBrightnessEnabled())
        showBrightnessControlEnabled(interactor.isManualBrightnessEnabled())
        showBrightnessPos(interactor.getManualBrightnessValue())
        showContentSizeText(interactor.getReadTextSize())
    }

    private fun showAutoBrightness(enabled: Boolean) = with(viewState) {
        showAutoBrightnessEnabled(enabled)
        showBrightnessControlEnabled(!enabled)
        setupBrightness()
    }
}