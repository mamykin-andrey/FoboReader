package ru.mamykin.foboreader.data.repository.devicebooks

import android.os.Environment
import ru.mamykin.foboreader.data.model.FileStructure
import ru.mamykin.foboreader.data.model.mapper.FileStructureMapper
import rx.Single
import java.io.File
import javax.inject.Inject

class DeviceBooksRepository @Inject constructor(
        private val mapper: FileStructureMapper
) {
    fun getRootDirectory(): String =
            Environment.getExternalStorageDirectory().absolutePath

    fun getFiles(path: String): Single<List<FileStructure>> =
            Single.just(File(path).listFiles().toList())
                    .map(mapper::transform)
}