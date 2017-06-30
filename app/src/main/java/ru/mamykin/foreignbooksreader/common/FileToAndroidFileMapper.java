package ru.mamykin.foreignbooksreader.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ru.mamykin.foreignbooksreader.models.AndroidFile;

/**
 * Creation date: 5/29/2017
 * Creation time: 12:38 PM
 * @author Andrey Mamykin(mamykin_av)
 */
public class FileToAndroidFileMapper {
    public List<AndroidFile> map(List<File> files) {
        List<AndroidFile> androidFiles = new ArrayList<>(files.size());
        for (File file : files) {
            androidFiles.add(new AndroidFile(file));
        }
        return androidFiles;
    }
}