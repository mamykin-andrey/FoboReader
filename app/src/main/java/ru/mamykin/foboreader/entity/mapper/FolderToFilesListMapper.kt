package ru.mamykin.foboreader.entity.mapper

import com.dropbox.core.v2.files.ListFolderResult
import ru.mamykin.foboreader.entity.DropboxFile
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