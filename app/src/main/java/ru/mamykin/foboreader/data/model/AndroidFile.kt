package ru.mamykin.foboreader.data.model

import ru.mamykin.foboreader.common.Utils

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
class AndroidFile(file: java.io.File) {
    var file: java.io.File? = null

    val name: String
        get() = file!!.name

    val absolutePath: String
        get() = file!!.absolutePath

    val isDirectory: Boolean
        get() = file!!.isDirectory

    /**
     * Получаем строку с атрибутами файла для отображения в списке
     * @return строка с атрибутами
     */
    val attributes: String
        get() = Utils.getSizeString(file!!.length()) + ", " + Utils.getLastModifiedString(file!!.lastModified())

    /**
     * Определяем, является ли файл книгой в формате FictionBook
     * @return true, если файл это книга FictionBook, false в остальных случаях
     */
    val isFictionBook: Boolean
        get() = file!!.name.endsWith(".fb2") || file!!.name.endsWith(".fbwt")

    init {
        this.file = file
    }

    override fun hashCode(): Int {
        return file!!.hashCode()
    }

    fun canRead(): Boolean {
        return file!!.canRead()
    }
}