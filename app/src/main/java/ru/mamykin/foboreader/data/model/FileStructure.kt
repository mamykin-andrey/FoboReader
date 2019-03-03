package ru.mamykin.foboreader.data.model

import java.io.File

data class FileStructure(
        val directoryName: String,
        val files: List<File>,
        val type: FileType,
        val isParentDirAvailable: Boolean
) {
    sealed class FileType {
        object File : FileType()
        object FictionBook : FileType()
        object Folder : FileType()
    }
}