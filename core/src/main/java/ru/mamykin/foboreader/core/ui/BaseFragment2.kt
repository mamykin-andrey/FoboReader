package ru.mamykin.foboreader.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ru.mamykin.foboreader.core.R
import ru.mamykin.foboreader.core.mvvm.BaseViewModel2
import ru.mamykin.foboreader.core.mvvm.SingleLiveEvent

abstract class BaseFragment2<VM : BaseViewModel2<ViewState, out Any, out Any, Effect>, ViewState, Effect>(
    @LayoutRes private val layoutId: Int
) : Fragment() {

    protected open val sharedElements: List<Pair<View, String>> = emptyList()
    protected var toolbar: Toolbar? = null

    protected abstract val viewModel: VM

    private var dataLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutId, container, false)
        .apply { initToolbar(this) }

    private fun initToolbar(
        view: View,
        showNavigationIcon: Boolean = false,
        navigationIconClicked: () -> Unit = { findNavController().navigateUp() }
    ) {
        view.findViewById<Toolbar>(R.id.toolbar)
            .also { toolbar = it }
            ?.takeIf { showNavigationIcon }
            ?.setNavigationOnClickListener { navigationIconClicked() }
            ?: run { toolbar?.navigationIcon = null }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initSharedTransition()
    }

    private fun initViewModel() {
        viewModel.stateLiveData.observe(::showState)
        viewModel.effectLiveData.observe(this::takeEffect)
        if (!dataLoaded) {
            viewModel.loadData()
            dataLoaded = true
        }
    }

    protected abstract fun showState(state: ViewState)

    protected abstract fun takeEffect(effect: Effect)

    private fun initSharedTransition() {
        sharedElements.forEach { (view, name) ->
            ViewCompat.setTransitionName(view, name)
        }
    }

    private fun <T> LiveData<T>.observe(observerFunc: (T) -> Unit) {
        observe(viewLifecycleOwner, Observer { observerFunc(it) })
    }

    private fun <T> SingleLiveEvent<T>.observe(observerFunc: (T) -> Unit) {
        observe(viewLifecycleOwner, Observer { observerFunc(it) })
    }
}