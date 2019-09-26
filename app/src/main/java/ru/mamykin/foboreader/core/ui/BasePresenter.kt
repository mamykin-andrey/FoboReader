package ru.mamykin.foboreader.core.ui

import androidx.annotation.StringRes
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.mamykin.foboreader.core.platform.ResourcesManager
import ru.mamykin.foboreader.core.platform.Schedulers
import rx.Completable
import rx.Single
import rx.Subscription
import rx.subscriptions.CompositeSubscription

@InjectViewState(view = BaseView::class)
abstract class BasePresenter<V : BaseView> : MvpPresenter<V>() {

    private val compositeSubscription = CompositeSubscription()
    protected abstract val schedulers: Schedulers
    protected abstract val resourcesManager: ResourcesManager

    override fun onDestroy() {
        super.onDestroy()
        compositeSubscription.clear()
    }

    protected fun onError(@StringRes errorRes: Int) {
        onError(resourcesManager.getString(errorRes))
    }

    protected fun onError(error: String) {
        viewState.onError(error)
    }

    protected fun Subscription.unsubscribeOnDestroy() {
        compositeSubscription.add(this)
    }

    protected fun <T> Single<T>.applySchedulers(): Single<T> =
            subscribeOn(schedulers.io())
                    .observeOn(schedulers.main())

    protected fun Completable.applySchedulers(): Completable =
            subscribeOn(schedulers.io())
                    .observeOn(schedulers.main())

    protected fun <T> Single<T>.showProgress(showProgressFunc: (Boolean) -> Unit = viewState::showLoading): Single<T> =
            doOnSubscribe { showProgressFunc(true) }
                    .doAfterTerminate { showProgressFunc(false) }
}