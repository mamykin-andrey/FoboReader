package ru.mamykin.foreignbooksreader.presenters

import com.arellomobile.mvp.InjectViewState

import java.io.File
import java.util.ArrayList
import java.util.Arrays
import java.util.Collections

import javax.inject.Inject

import ru.mamykin.foreignbooksreader.ReaderApp
import ru.mamykin.foreignbooksreader.common.FileToAndroidFileMapper
import ru.mamykin.foreignbooksreader.models.AndroidFile
import ru.mamykin.foreignbooksreader.views.DeviceBooksView

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@InjectViewState
class DeviceBooksPresenter(private var currentDir: String?) : BasePresenter<DeviceBooksView>() {
    @Inject
    lateinit var mapper: FileToAndroidFileMapper

    init {
        ReaderApp.component.inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        displayFiles(currentDir)
    }

    fun onFileClicked(file: AndroidFile) {
        if (!file.canRead()) {
            viewState.showPermissionMessage()
        } else if (file.isDirectory) {
            displayFiles(file.absolutePath)
        } else if (file.isFictionBook) {
            viewState.openBook(file.absolutePath)
        }
    }

    fun onUpClicked() {
        displayFiles(currentDir!!.substring(0, currentDir!!.lastIndexOf("/")))
    }

    fun displayFiles(currentDir: String?) {
        this.currentDir = currentDir
        val dir = File(currentDir!!)
        val filesList = mapper!!.map(ArrayList(Arrays.asList(*dir.listFiles())))
        Collections.sort(filesList) { o1, o2 -> getFileWeight(o2) - getFileWeight(o1) }
        val upFile = File(currentDir.substring(0, currentDir.lastIndexOf("/")))
        viewState.showUpDir(upFile.canRead() && upFile.isDirectory())
        viewState.setCurrentDir(currentDir)
        viewState.showFiles(filesList)
    }

    /**
     * Получаем "вес" файла для сортировки
     * @param file файл
     * @return приоритет файла в сортировке
     */
    private fun getFileWeight(file: AndroidFile): Int {
        if (file.isDirectory)
            return 2
        else if (file.isFictionBook)
            return 1
        return 0
    }
}