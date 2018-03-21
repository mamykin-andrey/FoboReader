package ru.mamykin.foboreader.presentation.splash

import ru.mamykin.foboreader.presentation.global.BasePresenter

class SplashPresenter : BasePresenter<SplashView>() {
    override fun attachView(view: SplashView) {
        super.attachView(view)

        view.startApplication()
    }
}