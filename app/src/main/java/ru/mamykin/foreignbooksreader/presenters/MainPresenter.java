package ru.mamykin.foreignbooksreader.presenters;

import com.arellomobile.mvp.InjectViewState;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import ru.mamykin.foreignbooksreader.ReaderApp;
import ru.mamykin.foreignbooksreader.events.RestartEvent;
import ru.mamykin.foreignbooksreader.preferences.PreferenceNames;
import ru.mamykin.foreignbooksreader.preferences.PreferencesManager;
import ru.mamykin.foreignbooksreader.views.MainView;

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@InjectViewState
public class MainPresenter extends BasePresenter<MainView> implements PreferenceNames {
    @Inject
    protected PreferencesManager pm;

    public MainPresenter() {
        ReaderApp.getComponent().inject(this);
    }

    @Override
    public void attachView(MainView view) {
        super.attachView(view);

        EventBus.getDefault().register(this);
        // Произошла смена темы в настройках
        onEvent(EventBus.getDefault().getStickyEvent(RestartEvent.class));
    }

    @Override
    public void detachView(MainView view) {
        super.detachView(view);

        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(RestartEvent e) {
        if (e != null) {
            EventBus.getDefault().removeStickyEvent(e.getClass());
            getViewState().restartActivity();
        }
    }
}