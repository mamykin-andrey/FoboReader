package ru.mamykin.foboreader.ui.global

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.mamykin.foboreader.ReaderApp
import ru.mamykin.foboreader.di.component.AppComponent

abstract class BaseFragment : MvpAppCompatFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
    }

    protected open fun injectDependencies() {
    }

    protected fun getAppComponent(): AppComponent {
        return (activity.application as ReaderApp).getAppComponent()
    }
}