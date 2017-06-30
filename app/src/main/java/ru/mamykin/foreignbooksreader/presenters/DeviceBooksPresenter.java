package ru.mamykin.foreignbooksreader.presenters;

import com.arellomobile.mvp.InjectViewState;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import ru.mamykin.foreignbooksreader.ReaderApp;
import ru.mamykin.foreignbooksreader.common.FileToAndroidFileMapper;
import ru.mamykin.foreignbooksreader.models.AndroidFile;
import ru.mamykin.foreignbooksreader.views.DeviceBooksView;

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@InjectViewState
public class DeviceBooksPresenter extends BasePresenter<DeviceBooksView> {
    private String currentDir;
    @Inject
    protected FileToAndroidFileMapper mapper;

    public DeviceBooksPresenter(String currentDir) {
        this.currentDir = currentDir;
        ReaderApp.getComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        displayFiles(currentDir);
    }

    public void onFileClicked(AndroidFile file) {
        if (!file.canRead()) {
            getViewState().showPermissionMessage();
        } else if (file.isDirectory()) {
            displayFiles(file.getAbsolutePath());
        } else if (file.isFictionBook()) {
            getViewState().openBook(file.getAbsolutePath());
        }
    }

    public void onUpClicked() {
        displayFiles(currentDir.substring(0, currentDir.lastIndexOf("/")));
    }

    public void displayFiles(String currentDir) {
        this.currentDir = currentDir;
        File dir = new File(currentDir);
        List<AndroidFile> filesList = mapper.map(new ArrayList<>(Arrays.asList(dir.listFiles())));
        Collections.sort(filesList, (o1, o2) -> getFileWeight(o2) - getFileWeight(o1));
        final File upFile = new File(currentDir.substring(0, currentDir.lastIndexOf("/")));
        getViewState().showUpDir(upFile.canRead() && upFile.isDirectory());
        getViewState().setCurrentDir(currentDir);
        getViewState().showFiles(filesList);
    }

    /**
     * Получаем "вес" файла для сортировки
     * @param file файл
     * @return приоритет файла в сортировке
     */
    private int getFileWeight(AndroidFile file) {
        if (file.isDirectory())
            return 2;
        else if (file.isFictionBook())
            return 1;
        return 0;
    }
}