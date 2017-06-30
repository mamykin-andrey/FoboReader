package ru.mamykin.foreignbooksreader.models;

import java.io.File;

import ru.mamykin.foreignbooksreader.common.Utils;

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
public class AndroidFile {
    private File file;

    public AndroidFile(java.io.File file) {
        this.file = file;
    }

    public java.io.File getFile() {
        return file;
    }

    public void setFile(java.io.File file) {
        this.file = file;
    }

    public String getName() {
        return file.getName();
    }

    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }

    public boolean isDirectory() {
        return file.isDirectory();
    }

    @Override
    public int hashCode() {
        return file.hashCode();
    }

    public boolean canRead() {
        return file.canRead();
    }

    /**
     * Получаем строку с атрибутами файла для отображения в списке
     * @return строка с атрибутами
     */
    public String getAttributes() {
        return Utils.getSizeString(file.length()) + ", " + Utils.getLastModifiedString(file.lastModified());
    }

    /**
     * Определяем, является ли файл книгой в формате FictionBook
     * @return true, если файл это книга FictionBook, false в остальных случаях
     */
    public boolean isFictionBook() {
        return file.getName().endsWith(".fb2") || file.getName().endsWith(".fbwt");
    }
}