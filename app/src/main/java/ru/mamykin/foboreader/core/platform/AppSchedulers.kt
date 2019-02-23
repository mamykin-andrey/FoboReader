package ru.mamykin.foboreader.core.platform

import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class AppSchedulers @Inject constructor() : ru.mamykin.foboreader.core.platform.Schedulers {

    override fun io(): Scheduler = Schedulers.io()

    override fun main(): Scheduler = AndroidSchedulers.mainThread()
}