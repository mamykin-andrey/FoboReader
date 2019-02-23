package ru.mamykin.foboreader.core.ui

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView

import rx.Subscription
import rx.subscriptions.CompositeSubscription

abstract class BasePresenter<V : MvpView> : MvpPresenter<V>() {

    private val compositeSubscription = CompositeSubscription()

    override fun onDestroy() {
        super.onDestroy()
        compositeSubscription.clear()
    }

    fun Subscription.unsubscribeOnDestroy() {
        compositeSubscription.add(this)
    }
}