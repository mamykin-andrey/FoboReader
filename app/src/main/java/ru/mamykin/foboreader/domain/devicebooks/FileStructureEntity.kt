package ru.mamykin.foboreader.domain.devicebooks

import java.io.File

data class FileStructureEntity(
        val files: List<File>,
        val isUpDirAvailable: Boolean,
        val currentDIr: String
)