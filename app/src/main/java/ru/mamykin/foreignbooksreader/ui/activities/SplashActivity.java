package ru.mamykin.foreignbooksreader.ui.activities;

import android.os.Bundle;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import ru.mamykin.foreignbooksreader.presenters.SplashPresenter;
import ru.mamykin.foreignbooksreader.views.SplashView;

/**
 * Страница со сплэш-скрином
 */
public class SplashActivity extends MvpAppCompatActivity implements SplashView {
    @InjectPresenter
    SplashPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // По-умолчанию View прикрепляется в onResume
        getMvpDelegate().onAttach();
    }

    @Override
    public void startApplication() {
        startActivity(MainActivity.getStartIntent(this));
        finish();
    }
}