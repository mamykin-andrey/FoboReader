package ru.mamykin.foreignbooksreader.extension

import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class Reactive {

    fun <T> Observable<T>.applySchedulers(observable: Observable<T>) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}