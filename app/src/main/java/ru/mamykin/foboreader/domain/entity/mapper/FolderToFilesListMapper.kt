package ru.mamykin.foboreader.domain.entity.mapper

import com.dropbox.core.v2.files.ListFolderResult
import ru.mamykin.foboreader.domain.entity.DropboxFile
import javax.inject.Inject

class FolderToFilesListMapper @Inject constructor() {

    fun transform(result: ListFolderResult): List<DropboxFile> =
            result.entries.map { DropboxFile(it) }
}