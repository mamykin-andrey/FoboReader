package ru.mamykin.foboreader.data.repository

import android.os.Environment
import ru.mamykin.foboreader.entity.AndroidFile
import rx.Single
import java.io.File
import javax.inject.Inject

class DeviceBooksRepository @Inject constructor() {

    fun getRootDirectory(): String {
        return Environment.getExternalStorageDirectory().absolutePath
    }

    fun getFiles(path: String): Single<List<AndroidFile>> {
        val dir = File(path)
        return Single.just(dir.listFiles())
                .map { it.map(::AndroidFile) }
    }

    fun canReadDirectory(path: String): Single<Boolean> {
        val dir = File(path)
        return Single.just(dir.canRead() && dir.isDirectory)
    }
}