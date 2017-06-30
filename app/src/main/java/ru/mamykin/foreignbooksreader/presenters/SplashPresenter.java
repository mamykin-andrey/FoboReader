package ru.mamykin.foreignbooksreader.presenters;

import ru.mamykin.foreignbooksreader.views.SplashView;

public class SplashPresenter extends BasePresenter<SplashView> {
    @Override
    public void attachView(SplashView view) {
        super.attachView(view);

        view.startApplication();
    }
}