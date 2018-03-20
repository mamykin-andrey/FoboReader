package ru.mamykin.foreignbooksreader.presenters

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView

import javax.inject.Inject

import rx.Subscription
import rx.subscriptions.CompositeSubscription

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
abstract class BasePresenter<V : MvpView> : MvpPresenter<V>() {
    private val subscription = CompositeSubscription()

    protected fun unsubscribeOnDestroy(subscription: Subscription) {
        this.subscription.add(subscription)
    }

    override fun onDestroy() {
        super.onDestroy()
        subscription.clear()
    }
}