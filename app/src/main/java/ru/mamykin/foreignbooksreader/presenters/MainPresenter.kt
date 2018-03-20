package ru.mamykin.foreignbooksreader.presenters

import com.arellomobile.mvp.InjectViewState

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

import javax.inject.Inject

import ru.mamykin.foreignbooksreader.ReaderApp
import ru.mamykin.foreignbooksreader.events.RestartEvent
import ru.mamykin.foreignbooksreader.preferences.PreferenceNames
import ru.mamykin.foreignbooksreader.preferences.PreferencesManager
import ru.mamykin.foreignbooksreader.views.MainView

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@InjectViewState
class MainPresenter : BasePresenter<MainView>(), PreferenceNames {

    @Inject
    lateinit var pm: PreferencesManager

    init {
        ReaderApp.component.inject(this)
    }

    override fun attachView(view: MainView) {
        super.attachView(view)

        EventBus.getDefault().register(this)
        // Произошла смена темы в настройках
        onEvent(EventBus.getDefault().getStickyEvent<RestartEvent>(RestartEvent::class.java!!))
    }

    override fun detachView(view: MainView) {
        super.detachView(view)

        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onEvent(e: RestartEvent?) {
        if (e != null) {
            EventBus.getDefault().removeStickyEvent(RestartEvent::class.java)
            viewState.restartActivity()
        }
    }
}