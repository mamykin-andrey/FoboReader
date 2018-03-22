package ru.mamykin.foboreader.presentation.global

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView

import rx.Subscription
import rx.subscriptions.CompositeSubscription

abstract class BasePresenter<V : MvpView> : MvpPresenter<V>() {

    private val compositeSubscription = CompositeSubscription()

    protected fun unsubscribeOnDestroy(subscription: Subscription) {
        this.compositeSubscription.add(subscription)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeSubscription.clear()
    }

    fun Subscription.unsubscrubeOnDestory() {
        compositeSubscription.add(this)
    }
}