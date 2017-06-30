package ru.mamykin.foreignbooksreader.models;

import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.Metadata;

import ru.mamykin.foreignbooksreader.common.Utils;

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
public class DropboxFile {
    private Metadata file;
    private boolean upDir;

    public DropboxFile(Metadata file, boolean upDir) {
        this.file = file;
        this.upDir = upDir;
    }

    public Metadata getFile() {
        return file;
    }

    public void setFile(Metadata file) {
        this.file = file;
    }

    public boolean isUpDir() {
        return upDir;
    }

    public void setUpDir(boolean upDir) {
        this.upDir = upDir;
    }

    public String getName() {
        return file.getName();
    }

    public String getPathLower() {
        return file.getPathLower();
    }

    public boolean isDirectory() {
        return file instanceof FolderMetadata;
    }

    /**
     * Получаем строку с атрибутами файла для отображения в списке
     * @return строка с атрибутами
     */
    public String getAttributes() {
        FileMetadata fileData = (FileMetadata) file;
        return Utils.getSizeString(fileData.getSize()) + ", "
                + Utils.getLastModifiedString(fileData.getServerModified().getTime());
    }

    /**
     * Определяем, является ли файл книгой в формате FictionBook
     * @return true, если файл это книга FictionBook, false в остальных случаях
     */
    public boolean isFictionBook() {
        return file.getName().endsWith(".fb2") || file.getName().endsWith(".fbwt");
    }
}