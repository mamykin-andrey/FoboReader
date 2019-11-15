package ru.mamykin.foboreader.core.mvvm

import androidx.lifecycle.ViewModel
import ru.mamykin.foboreader.core.platform.Schedulers
import rx.Single
import rx.Subscription
import rx.subscriptions.CompositeSubscription

abstract class BaseViewModel : ViewModel() {

    protected abstract val schedulers: Schedulers
    private val compositeSubscription = CompositeSubscription()

    protected fun Subscription.unsubscribeOnDestroy() {
        compositeSubscription.add(this)
    }

    protected fun <T> Single<T>.applySchedulers(): Single<T> =
            subscribeOn(schedulers.io())
                    .observeOn(schedulers.main())
}