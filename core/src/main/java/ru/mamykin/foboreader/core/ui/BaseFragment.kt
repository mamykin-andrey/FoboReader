package ru.mamykin.foboreader.core.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import ru.mamykin.foboreader.core.R
import ru.mamykin.foboreader.core.mvvm.BaseViewModel
import ru.mamykin.foboreader.core.mvvm.SingleLiveEvent

abstract class BaseFragment<VM : BaseViewModel<ViewState, out Any, out Any, Effect>, ViewState, Effect> : Fragment() {

    protected abstract val viewModel: VM

    protected val toolbar: Toolbar
        get() = view!!.findViewById(R.id.toolbar)
            ?: throw IllegalStateException("Couldn't find toolbar in layout!")

    protected val progressView: ContentLoadingProgressBar
        get() = view!!.findViewById(R.id.cpb_content_loading)
            ?: throw IllegalStateException("Couldn't find progress view in layout!")

    private var dataLoaded = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.stateLiveData.observe(::showState)
        viewModel.effectLiveData.observe(this::takeEffect)
        if (!dataLoaded) {
            viewModel.loadData()
            dataLoaded = true
        }
    }

    protected open fun showState(state: ViewState) {}

    protected open fun takeEffect(effect: Effect) {}

    private fun <T> LiveData<T>.observe(observerFunc: (T) -> Unit) {
        observe(viewLifecycleOwner, Observer { observerFunc(it) })
    }

    private fun <T> SingleLiveEvent<T>.observe(observerFunc: (T) -> Unit) {
        observe(viewLifecycleOwner, Observer { observerFunc(it) })
    }
}