package ru.mamykin.foboreader.presentation.main

import com.arellomobile.mvp.InjectViewState
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import ru.mamykin.foboreader.common.events.RestartEvent
import ru.mamykin.foboreader.presentation.global.BasePresenter

@InjectViewState
class MainPresenter : BasePresenter<MainView>() {

    override fun attachView(view: MainView) {
        super.attachView(view)

        EventBus.getDefault().register(this)
        // Произошла смена темы в настройках
        onEvent(EventBus.getDefault().getStickyEvent(RestartEvent::class.java))
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