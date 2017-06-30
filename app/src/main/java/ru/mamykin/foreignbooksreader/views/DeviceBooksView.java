package ru.mamykin.foreignbooksreader.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.mamykin.foreignbooksreader.models.AndroidFile;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface DeviceBooksView extends MvpView {
    void showFiles(List<AndroidFile> files);

    void setCurrentDir(String currentDir);

    void showPermissionMessage();

    void openBook(String path);

    void showUpDir(boolean show);
}