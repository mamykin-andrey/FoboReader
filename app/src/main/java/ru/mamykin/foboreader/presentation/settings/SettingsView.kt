package ru.mamykin.foboreader.presentation.settings

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SettingsView : MvpView {

    fun showDropboxAccount(account: String?)

    fun showContentSizeText(size: Int)

    fun showBrightnessPos(position: Int)

    fun showAutoBrightnessEnabled(enabled: Boolean)

    fun showBrightnessControlEnabled(enabled: Boolean)

    fun showNightThemeEnabled(enabled: Boolean)

    fun restartActivity()

    fun setupBrightness()
}