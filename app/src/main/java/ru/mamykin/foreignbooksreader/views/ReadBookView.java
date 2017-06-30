package ru.mamykin.foreignbooksreader.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ReadBookView extends MvpView {
    void showToast(String text);

    void showLoading(boolean loading);

    void setBookName(String name);

    void setReadPages(String text);

    void setReadPercent(String text);

    void setSourceText(CharSequence text);

    void setTranslationText(String text);

    void showTranslationPopup(String original, String translation);

    void initBookView(String title, String text);

    void showBookContent(boolean show);
}