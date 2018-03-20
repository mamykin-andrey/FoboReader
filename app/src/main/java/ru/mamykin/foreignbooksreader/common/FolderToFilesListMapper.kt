package ru.mamykin.foreignbooksreader.common

import com.dropbox.core.v2.files.ListFolderResult

import java.util.ArrayList

import ru.mamykin.foreignbooksreader.data.model.DropboxFile
import rx.functions.Func1

class FolderToFilesListMapper : Func1<ListFolderResult, List<DropboxFile>> {

    override fun call(result: ListFolderResult): List<DropboxFile> {
        val metaDataList = result.entries
        val filesList = ArrayList<DropboxFile>(metaDataList.size)
        for (metadata in metaDataList) {
            filesList.add(DropboxFile(metadata, false))
        }
        return filesList
    }
}