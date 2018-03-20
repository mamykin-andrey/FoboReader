package ru.mamykin.foreignbooksreader.presentation.device

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

import ru.mamykin.foreignbooksreader.models.AndroidFile

@StateStrategyType(AddToEndSingleStrategy::class)
interface DeviceBooksView : MvpView {
    fun showFiles(files: List<AndroidFile>)

    fun setCurrentDir(currentDir: String)

    fun showPermissionMessage()

    fun openBook(path: String)

    fun showUpDir(show: Boolean)
}