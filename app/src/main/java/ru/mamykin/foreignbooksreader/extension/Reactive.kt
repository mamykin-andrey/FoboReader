package ru.mamykin.foreignbooksreader.extension

import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

fun <T> Observable<T>.applySchedulers(): Observable<T> =
        subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())