package ru.mamykin.foboreader.domain.devicebooks

import ru.mamykin.foboreader.entity.AndroidFile

data class FileStructureEntity(
        val files: List<AndroidFile>,
        val isUpDirAvailable: Boolean,
        val currentDIr: String
)