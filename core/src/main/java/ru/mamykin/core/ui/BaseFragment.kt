package ru.mamykin.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import ru.mamykin.core.R
import ru.mamykin.core.di.ComponentHolder

abstract class BaseFragment(
        @LayoutRes private val layoutId: Int
) : Fragment() {

    protected var toolbar: Toolbar? = null

    inline fun <reified T : ViewModel> viewModel(): Lazy<T> = lazy { getViewModel<T>() }

    inline fun <reified T : ViewModel> getViewModel(): T = null!!

    protected fun getAppComponent() = (activity?.application as ComponentHolder).getAppComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getAppComponent().inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layoutId, container, false)
        toolbar = view.findViewById(R.id.toolbar)
        return view
    }
}