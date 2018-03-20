package ru.mamykin.foreignbooksreader.common

import java.io.File
import java.util.ArrayList

import ru.mamykin.foreignbooksreader.models.AndroidFile

class FileToAndroidFileMapper {
    fun map(files: List<File>): List<AndroidFile> {
        val androidFiles = ArrayList<AndroidFile>(files.size)
        for (file in files) {
            androidFiles.add(AndroidFile(file))
        }
        return androidFiles
    }
}