package ru.mamykin.foboreader.entity.mapper

import ru.mamykin.foboreader.entity.AndroidFile
import java.io.File
import javax.inject.Inject

class FileToAndroidFileMapper @Inject constructor() {

    fun transform(file: File): AndroidFile = AndroidFile(file)

    fun transform(files: Array<File>): List<AndroidFile> = files.map(this::transform)
}