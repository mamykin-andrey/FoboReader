package ru.mamykin.foboreader.core.presentation

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import ru.mamykin.foboreader.core.R

abstract class BaseFragment<VM : BaseViewModel<ViewState, out Any, out Any, Effect>, ViewState, Effect>(
    @LayoutRes layoutRes: Int
) : Fragment(layoutRes) {

    protected abstract val viewModel: VM

    protected val toolbar: Toolbar?
        get() = requireView().findViewById(R.id.toolbar)

    protected val progressView: FrameLayout
        get() = requireView().findViewById(R.id.fl_content_loading)
            ?: throw IllegalStateException("Couldn't find progress view in layout!")

    private var dataLoaded = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar?.setNavigationOnClickListener { requireActivity().onBackPressed() }
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
        observe(viewLifecycleOwner, { observerFunc(it) })
    }

    private fun <T> SingleLiveEvent<T>.observe(observerFunc: (T) -> Unit) {
        observe(viewLifecycleOwner, { observerFunc(it) })
    }
}