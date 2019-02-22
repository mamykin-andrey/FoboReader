package ru.mamykin.foboreader.presentation.dropbox

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.mamykin.foboreader.entity.DropboxFile
import ru.mamykin.foboreader.presentation.global.BaseView

@StateStrategyType(AddToEndSingleStrategy::class)
interface DropboxView : BaseView {

    fun showFiles(files: List<DropboxFile>)

    fun showLoadingItem(position: Int)

    fun hideLoadingItem()

    fun showLoading(show: Boolean)

    fun showAuth()
}