package ru.mamykin.foreignbooksreader.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface BookDetailsView extends MvpView {
    void openBook(int bookId);

    void showTitle(String title);

    void setHomeEnabled(boolean enabled);

    void showBookName(String name);

    void showBookAuthor(String author);

    void showBookPath(String path);

    void showBookCurrentPage(String currentPage);

    void showBookGenre(String genre);

    void showBookOriginalLang(String lang);

    void showBookCreatedDate(String date);
}