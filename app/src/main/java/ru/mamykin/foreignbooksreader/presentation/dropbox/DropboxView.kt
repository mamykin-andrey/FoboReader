package ru.mamykin.foreignbooksreader.presentation.dropbox

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

import ru.mamykin.foreignbooksreader.data.model.DropboxFile

@StateStrategyType(AddToEndSingleStrategy::class)
interface DropboxView : MvpView {
    fun showFiles(filesList: List<DropboxFile>)

    fun hideFiles()

    fun openBook(path: String)

    fun showLoadingItem(position: Int)

    fun hideLoadingItem()

    fun showLoading()

    fun hideLoading()

    fun showCurrentDir(dir: String)

    fun showAuth()

    fun hideAuth()

    fun showUpButton(show: Boolean)
}