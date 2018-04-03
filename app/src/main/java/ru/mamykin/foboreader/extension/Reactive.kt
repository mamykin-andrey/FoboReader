package ru.mamykin.foboreader.extension

import rx.Single
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

fun <T> Single<T>.applySchedulers(): Single<T> =
        subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())