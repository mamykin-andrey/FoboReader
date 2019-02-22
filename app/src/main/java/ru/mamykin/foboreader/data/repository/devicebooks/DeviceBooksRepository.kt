package ru.mamykin.foboreader.data.repository.devicebooks

import android.os.Environment
import rx.Single
import java.io.File
import javax.inject.Inject

class DeviceBooksRepository @Inject constructor() {

    fun getRootDirectory(): String =
            Environment.getExternalStorageDirectory().absolutePath

    fun getFiles(path: String): Single<List<File>> = Single.just(
            File(path).listFiles().toList()
    )

    fun canReadDirectory(path: String): Single<Boolean> = Single.just(
            File(path).let { it.isDirectory && it.canRead() }
    )
}