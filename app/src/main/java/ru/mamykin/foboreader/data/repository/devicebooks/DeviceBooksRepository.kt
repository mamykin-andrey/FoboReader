package ru.mamykin.foboreader.data.repository.devicebooks

import android.os.Environment
import ru.mamykin.foboreader.data.model.FileStructure
import ru.mamykin.foboreader.data.model.mapper.FileStructureMapper
import java.io.File
import javax.inject.Inject

class DeviceBooksRepository @Inject constructor(
        private val mapper: FileStructureMapper
) {
    fun getRootDirectory(): String = Environment.getExternalStorageDirectory().absolutePath

    fun getFiles(path: String): List<FileStructure> {
        return mapper.transform(File(path).listFiles().toList())
    }
}