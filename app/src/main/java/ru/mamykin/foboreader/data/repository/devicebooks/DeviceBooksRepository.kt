package ru.mamykin.foboreader.data.repository.devicebooks

import android.os.Environment
import rx.Single
import java.io.File
import javax.inject.Inject

class DeviceBooksRepository @Inject constructor() {

    fun getRootDirectory(): String = Environment.getExternalStorageDirectory().absolutePath

    fun getFiles(path: String): Single<List<File>> {
        val dir = File(path)
        return Single.just(dir.listFiles().toList())
    }

    fun canReadDirectory(path: String): Single<Boolean> {
        val dir = File(path)
        return Single.just(dir.canRead() && dir.isDirectory)
    }
}