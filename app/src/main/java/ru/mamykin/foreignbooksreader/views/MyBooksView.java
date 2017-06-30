package ru.mamykin.foreignbooksreader.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.mamykin.foreignbooksreader.models.FictionBook;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface MyBooksView extends MvpView {
    void showEmptyStateView();

    void showBooksList(List<FictionBook> booksList);

    void openBook(int bookId);

    void openBookDetails(int bookId);

    void showBookShareDialog(String bookName, String url);

    void showBookShareDialog(String bookName);
}