package ru.mamykin.foboreader.entity

import com.dropbox.core.v2.files.FileMetadata
import com.dropbox.core.v2.files.FolderMetadata
import com.dropbox.core.v2.files.Metadata

import ru.mamykin.foboreader.domain.Utils

class DropboxFile(var file: Metadata?, var isUpDir: Boolean) {

    val name: String
        get() = file!!.name

    val pathLower: String
        get() = file!!.pathLower

    val isDirectory: Boolean
        get() = file is FolderMetadata

    /**
     * Получаем строку с атрибутами файла для отображения в списке
     * @return строка с атрибутами
     */
    val attributes: String
        get() {
            val fileData = file as FileMetadata?
            return (Utils.getSizeString(fileData!!.size) + ", "
                    + Utils.getLastModifiedString(fileData.serverModified.time))
        }

    /**
     * Определяем, является ли файл книгой в формате FictionBook
     * @return true, если файл это книга FictionBook, false в остальных случаях
     */
    val isFictionBook: Boolean
        get() = file!!.name.endsWith(".fb2") || file!!.name.endsWith(".fbwt")
}