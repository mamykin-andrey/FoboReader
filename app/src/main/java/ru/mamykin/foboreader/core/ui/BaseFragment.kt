package ru.mamykin.foboreader.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import ru.mamykin.foboreader.ReaderApp
import ru.mamykin.foboreader.core.di.component.AppComponent
import javax.inject.Inject

abstract class BaseFragment(
        @LayoutRes private val layoutId: Int
) : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    inline fun <reified T : ViewModel> viewModel(): Lazy<T> = lazy { getViewModel<T>() }

    inline fun <reified T : ViewModel> getViewModel(): T =
            ViewModelProviders.of(this, viewModelFactory)[T::class.java]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getAppComponent().inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutId, container, false)

    protected fun getAppComponent(): AppComponent =
            (activity!!.application as ReaderApp).appComponent
}