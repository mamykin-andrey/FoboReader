package ru.mamykin.foreignbooksreader.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.mamykin.foreignbooksreader.models.StoreBook;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface BooksStoreView extends MvpView {
    void showBooks(List<StoreBook> booksList);

    void showMessage(String message);

    void showLoading(boolean show);
}