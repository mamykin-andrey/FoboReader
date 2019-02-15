package ru.mamykin.foboreader.presentation.devicebooks

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import java.io.File

@StateStrategyType(AddToEndSingleStrategy::class)
interface DeviceBooksView : MvpView {

    fun showNoPermissionView()

    fun showFiles(files: List<File>)

    fun showCurrentDir(currentDir: String)

    fun showPermissionError()

    fun showBookFormatError()

    fun showParentDirAvailable(show: Boolean)
}