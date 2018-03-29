package ru.mamykin.foboreader.presentation.settings

import com.arellomobile.mvp.InjectViewState
import ru.mamykin.foboreader.domain.settings.AppSettingsEntity
import ru.mamykin.foboreader.domain.settings.SettingsInteractor
import ru.mamykin.foboreader.presentation.global.BasePresenter
import javax.inject.Inject

@InjectViewState
class SettingsPresenter @Inject constructor(
        private val interactor: SettingsInteractor
) : BasePresenter<SettingsView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        loadSettings()
    }

    fun onNightThemeEnabled(isChecked: Boolean) {
        interactor.enableNightTheme(isChecked)
                .subscribe(Throwable::printStackTrace, viewState::restartActivity)
                .unsubscribeOnDestory()
    }

    fun onAutoBrightnessEnabled(isChecked: Boolean) {
        interactor.enableAutoBrightness(isChecked)
                .subscribe(this::displayAutoBrightness, Throwable::printStackTrace)
                .unsubscribeOnDestory()
    }

    fun onBrightnessProgressChanged(progress: Int) {
        interactor.changeBrightness(progress)
                .subscribe(Throwable::printStackTrace, viewState::setupBrightness)
                .unsubscribeOnDestory()
    }

    fun onDropboxLogoutSelected() {
        interactor.logoutDropbox()
                .subscribe({ it.printStackTrace() }, { viewState.setDropboxAccount("") })
                .unsubscribeOnDestory()
    }

    private fun loadSettings() {
        interactor.getSettings()
                .subscribe(this::displaySettings, Throwable::printStackTrace)
                .unsubscribeOnDestory()
    }

    private fun displaySettings(appSettings: AppSettingsEntity) {
        viewState.setNightThemeEnabled(appSettings.nightThemeEnabled)
        viewState.setAutoBrightnessChecked(!appSettings.manualBrightnessEnabled)
        viewState.setBrightnessControlEnabled(appSettings.manualBrightnessEnabled)
        viewState.setBrightnessPos(appSettings.manualBrightnessValue)
        viewState.setContentSizeText(appSettings.readTextSize)
        viewState.setDropboxAccount(appSettings.dropboxAccount)
    }

    private fun displayAutoBrightness(enabled: Boolean) {
        viewState.setAutoBrightnessChecked(enabled)
        viewState.setBrightnessControlEnabled(!enabled)
        viewState.setupBrightness()
    }
}