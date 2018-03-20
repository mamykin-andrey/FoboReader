package ru.mamykin.foreignbooksreader.common;

import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;

import java.util.ArrayList;
import java.util.List;

import ru.mamykin.foreignbooksreader.models.DropboxFile;
import rx.functions.Func1;

public class FolderToFilesListMapper implements Func1<ListFolderResult, List<DropboxFile>> {
    @Override
    public List<DropboxFile> call(ListFolderResult result) {
        List<Metadata> metadatasList = result.getEntries();
        List<DropboxFile> filesList = new ArrayList<>(metadatasList.size());
        for (Metadata metadata : metadatasList) {
            filesList.add(new DropboxFile(metadata, false));
        }
        return filesList;
    }
}