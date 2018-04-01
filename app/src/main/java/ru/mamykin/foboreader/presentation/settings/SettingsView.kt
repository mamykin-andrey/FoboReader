package ru.mamykin.foboreader.presentation.settings

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SettingsView : MvpView {

    fun setDropboxAccount(account: String)

    fun setContentSizeText(size: Int)

    fun setBrightnessPos(position: Int)

    fun setAutoBrightnessChecked(enabled: Boolean)

    fun setBrightnessControlEnabled(enabled: Boolean)

    fun setNightThemeEnabled(enabled: Boolean)

    fun setTitle(title: String)

    fun setHomeAsUpEnabled(enabled: Boolean)

    fun restartActivity()

    fun setupBrightness()
}