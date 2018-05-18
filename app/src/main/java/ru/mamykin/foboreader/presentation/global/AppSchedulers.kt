package ru.mamykin.foboreader.presentation.global

import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class AppSchedulers @Inject constructor() : ru.mamykin.foboreader.presentation.global.Schedulers {

    override fun io(): Scheduler = Schedulers.io()

    override fun main(): Scheduler = AndroidSchedulers.mainThread()
}