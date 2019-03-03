package ru.mamykin.foboreader.data.model.mapper

import ru.mamykin.foboreader.data.model.FileStructure
import java.io.File
import javax.inject.Inject

class FileStructureMapper @Inject constructor() {

    fun transform(files: List<File>): List<FileStructure> {
        return files.map {
            FileStructure(
                    it.name,
                    it.listFiles().toList(),
                    getType(it),
                    canOpenDirectory(it.absolutePath)
            )
        }
    }

    private fun canOpenDirectory(path: String): Boolean = File(path).let { it.isDirectory && it.canRead() }

    private fun getType(file: File): FileStructure.FileType = when {
        file.name.endsWith(".fb2") || file.name.endsWith(".fbwt") -> FileStructure.FileType.FictionBook
        file.isDirectory -> FileStructure.FileType.Folder
        else -> FileStructure.FileType.File
    }
}