package ru.mamykin.foboreader.presentation.about

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface AboutView : MvpView {
    fun showAppVersion(version: String)

    fun openWebsite(url: String)

    fun showFeedbackDialog(email: String, subject: String, text: String)

    fun showChangelogDialog(text: String)

    fun showLibrariesDialog()

    fun showToast(text: String)
}