package ru.mamykin.foboreader.presentation.devicebooks

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

import ru.mamykin.foboreader.entity.AndroidFile

@StateStrategyType(AddToEndSingleStrategy::class)
interface DeviceBooksView : MvpView {

    fun showFiles(files: List<AndroidFile>)

    fun showCurrentDir(currentDir: String)

    fun showPermissionMessage()

    fun showUpDir(show: Boolean)
}