package ru.mamykin.foreignbooksreader.presentation.splash

import ru.mamykin.foreignbooksreader.presentation.global.BasePresenter

class SplashPresenter : BasePresenter<SplashView>() {
    override fun attachView(view: SplashView) {
        super.attachView(view)

        view.startApplication()
    }
}