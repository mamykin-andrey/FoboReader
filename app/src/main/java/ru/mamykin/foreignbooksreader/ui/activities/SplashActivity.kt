package ru.mamykin.foreignbooksreader.ui.activities

import android.os.Bundle

import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter

import ru.mamykin.foreignbooksreader.presenters.SplashPresenter
import ru.mamykin.foreignbooksreader.views.SplashView

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