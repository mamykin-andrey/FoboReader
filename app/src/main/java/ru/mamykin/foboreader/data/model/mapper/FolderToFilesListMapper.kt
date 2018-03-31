package ru.mamykin.foboreader.data.model.mapper

import com.dropbox.core.v2.files.ListFolderResult
import ru.mamykin.foboreader.data.model.DropboxFile
import java.util.*

class FolderToFilesListMapper {

    fun transform(result: ListFolderResult): List<DropboxFile> {
        val metaDataList = result.entries
        val filesList = ArrayList<DropboxFile>(metaDataList.size)
        for (metadata in metaDataList) {
            filesList.add(DropboxFile(metadata, false))
        }
        return filesList
    }
}