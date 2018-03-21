package ru.mamykin.foboreader.ui.splash

import android.os.Bundle

import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import ru.mamykin.foboreader.presentation.splash.SplashPresenter

import ru.mamykin.foboreader.ui.main.MainActivity
import ru.mamykin.foboreader.presentation.splash.SplashView

/**
 * Страница со сплэш-скрином
 */
class SplashActivity : MvpAppCompatActivity(), SplashView {
    @InjectPresenter
    internal var presenter: SplashPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // По-умолчанию View прикрепляется в onResume
        mvpDelegate.onAttach()
    }

    override fun startApplication() {
        startActivity(MainActivity.getStartIntent(this))
        finish()
    }
}