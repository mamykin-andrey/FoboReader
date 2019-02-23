package ru.mamykin.foboreader.core.extension

import rx.Completable
import rx.Scheduler
import rx.Single
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

fun <T> Single<T>.applySchedulers(): Single<T> =
        subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

fun Completable.applySchedulers(): Completable =
        subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.applySchedulers(subscriberScheduler: Scheduler,
                                  observerScheduler: Scheduler): Single<T> {
    return subscribeOn(subscriberScheduler)
            .observeOn(observerScheduler)
}