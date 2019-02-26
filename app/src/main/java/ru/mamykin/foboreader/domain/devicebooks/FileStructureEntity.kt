package ru.mamykin.foboreader.domain.devicebooks

import java.io.File

data class FileStructureEntity(
        val directoryName: String,
        val files: List<File>,
        val isParentDirAvailable: Boolean
)