package ru.mamykin.foboreader.core.ui

import android.support.annotation.StringRes
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.mamykin.foboreader.core.platform.ResourcesManager
import ru.mamykin.foboreader.core.platform.Schedulers
import rx.Single
import rx.Subscription
import rx.subscriptions.CompositeSubscription

@InjectViewState
abstract class BasePresenter<V : BaseView> : MvpPresenter<V>() {

    private val compositeSubscription = CompositeSubscription()
    protected abstract val schedulers: Schedulers
    protected abstract val resourcesManager: ResourcesManager

    override fun onDestroy() {
        super.onDestroy()
        compositeSubscription.clear()
    }

    protected fun onError(@StringRes errorStr: Int) {
        viewState.onError(resourcesManager.getString(errorStr))
    }

    protected fun Subscription.unsubscribeOnDestroy() {
        compositeSubscription.add(this)
    }

    protected fun <T> Single<T>.applySchedulers() =
            subscribeOn(schedulers.io())
                    .observeOn(schedulers.main())

    protected fun <T> Single<T>.showProgress(showProgressFunc: (Boolean) -> Unit = viewState::showLoading) =
            doOnSubscribe { showProgressFunc(true) }
                    .doAfterTerminate { showProgressFunc(false) }
}