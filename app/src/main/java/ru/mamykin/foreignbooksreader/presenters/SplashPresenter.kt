package ru.mamykin.foreignbooksreader.presenters

import ru.mamykin.foreignbooksreader.views.SplashView

class SplashPresenter : BasePresenter<SplashView>() {
    override fun attachView(view: SplashView) {
        super.attachView(view)

        view.startApplication()
    }
}