package ru.mamykin.foboreader.presentation.settings

import com.arellomobile.mvp.InjectViewState
import ru.mamykin.foboreader.domain.settings.SettingsInteractor
import ru.mamykin.foboreader.presentation.global.BasePresenter
import javax.inject.Inject

@InjectViewState
class SettingsPresenter @Inject constructor(
        private val interactor: SettingsInteractor
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

    fun onDropboxLogoutSelected() {
        interactor.logoutDropbox()
    }

    private fun showSettings() {
        viewState.showNightThemeEnabled(interactor.isNightThemeEnabled())
        viewState.showAutoBrightnessEnabled(!interactor.isManualBrightnessEnabled())
        viewState.showBrightnessControlEnabled(interactor.isManualBrightnessEnabled())
        viewState.showBrightnessPos(interactor.getManualBrightnessValue())
        viewState.showContentSizeText(interactor.getReadTextSize())
        viewState.showDropboxAccount(interactor.getDropboxAccount())
    }

    private fun showAutoBrightness(enabled: Boolean) {
        viewState.showAutoBrightnessEnabled(enabled)
        viewState.showBrightnessControlEnabled(!enabled)
        viewState.setupBrightness()
    }
}