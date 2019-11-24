package ru.mamykin.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import ru.mamykin.core.di.ComponentHolder

abstract class BaseFragment(
        @LayoutRes private val layoutId: Int
) : Fragment() {

    val viewModelFactory: ViewModelProvider.Factory by lazy {
        (activity!!.application as ComponentHolder)
                .dependenciesProvider()
                .viewModelFactory()
    }

    inline fun <reified T : ViewModel> viewModel(): Lazy<T> = lazy { getViewModel<T>() }

    inline fun <reified T : ViewModel> getViewModel(): T =
            ViewModelProviders.of(this, viewModelFactory)[T::class.java]

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutId, container, false)
}