package ru.mamykin.foreignbooksreader.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface AboutView extends MvpView {
    void showAppVersion(String version);

    void openWebsite(String url);

    void showFeedbackDialog(String email, String subject, String text);

    void showChangelogDialog(String text);

    void showLibrariesDialog();

    void showToast(String text);
}