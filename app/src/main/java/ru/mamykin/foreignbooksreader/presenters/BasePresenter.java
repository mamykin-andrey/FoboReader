package ru.mamykin.foreignbooksreader.presenters;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
public abstract class BasePresenter<V extends MvpView> extends MvpPresenter<V> {
    private CompositeSubscription subscription = new CompositeSubscription();

    protected void unsubscribeOnDestroy(@NonNull Subscription subscription) {
        this.subscription.add(subscription);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscription.clear();
    }
}