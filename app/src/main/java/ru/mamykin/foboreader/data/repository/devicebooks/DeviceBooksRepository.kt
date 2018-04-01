package ru.mamykin.foboreader.data.repository.devicebooks

import android.os.Environment
import ru.mamykin.foboreader.entity.AndroidFile
import ru.mamykin.foboreader.entity.mapper.FileToAndroidFileMapper
import rx.Single
import java.io.File
import javax.inject.Inject

class DeviceBooksRepository @Inject constructor(
        private val mapper: FileToAndroidFileMapper
) {
    fun getRootDirectory(): String {
        return Environment.getExternalStorageDirectory().absolutePath
    }

    fun getFiles(path: String): Single<List<AndroidFile>> {
        val dir = File(path)
        return Single.just(dir.listFiles())
                .map(mapper::transform)
    }

    fun canReadDirectory(path: String): Single<Boolean> {
        val dir = File(path)
        return Single.just(dir.canRead() && dir.isDirectory)
    }
}