package ru.mamykin.foboreader.presentation.dropbox

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

import ru.mamykin.foboreader.entity.DropboxFile

@StateStrategyType(AddToEndSingleStrategy::class)
interface DropboxView : MvpView {

    fun showFiles(files: List<DropboxFile>)

    fun showLoadingItem(position: Int?)

    fun showLoading(show: Boolean)

    fun showAuth(show: Boolean)
}