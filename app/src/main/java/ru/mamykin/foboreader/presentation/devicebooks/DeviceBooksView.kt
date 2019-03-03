package ru.mamykin.foboreader.presentation.devicebooks

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.mamykin.foboreader.core.ui.BaseView
import java.io.File

@StateStrategyType(AddToEndSingleStrategy::class)
interface DeviceBooksView : BaseView {

    fun showNoPermissionView()

    fun showFiles(files: List<File>)

    fun showCurrentDir(currentDir: String)

    fun showParentDirAvailable(show: Boolean)
}