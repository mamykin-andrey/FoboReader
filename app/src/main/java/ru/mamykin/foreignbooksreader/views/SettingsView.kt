package ru.mamykin.foreignbooksreader.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SettingsView : MvpView {
    fun setDropboxAccount(account: String)

    fun showDropboxLogoutDialog()

    fun setContentSizeText(text: String)

    fun setBrightnessPos(position: Int)

    fun setAutoBrightnessChecked(enabled: Boolean)

    fun setBrightnessControlEnabled(enabled: Boolean)

    fun setNightThemeEnabled(enabled: Boolean)

    fun setTitle(title: String)

    fun setHomeAsUpEnabled(enabled: Boolean)

    fun restartActivity()

    fun setupBrightness()
}