package ru.mamykin.foreignbooksreader.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.mamykin.foreignbooksreader.models.DropboxFile;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface DropboxView extends MvpView {
    void showFiles(List<DropboxFile> filesList);

    void hideFiles();

    void openBook(String path);

    void showLoadingItem(int position);

    void hideLoadingItem();

    void showLoading();

    void hideLoading();

    void showCurrentDir(String dir);

    void showAuth();

    void hideAuth();

    void showUpButton(boolean show);
}