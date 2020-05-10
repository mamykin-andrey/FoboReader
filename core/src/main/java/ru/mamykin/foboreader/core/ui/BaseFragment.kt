package ru.mamykin.foboreader.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.mamykin.foboreader.core.R

abstract class BaseFragment(
        @LayoutRes private val layoutId: Int
) : Fragment() {

    protected open val sharedElements: List<Pair<View, String>> = emptyList()

    protected var toolbar: Toolbar? = null
    protected open val showNavigationIcon: Boolean
        get() = false

    private var dataLoaded = false

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutId, container, false)
            .apply { initToolbar(this) }

    private fun initToolbar(view: View) {
        view.findViewById<Toolbar>(R.id.toolbar)
                .also { toolbar = it }
                ?.takeIf { showNavigationIcon }
                ?.setNavigationOnClickListener { onNavigationIconClicked() }
                ?: run { toolbar?.navigationIcon = null }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLoadData()
        initSharedTransition()
    }

    private fun initLoadData() {
        if (!dataLoaded) {
            loadData()
            dataLoaded = true
        }
    }

    private fun initSharedTransition() {
        sharedElements.forEach { (view, name) ->
            ViewCompat.setTransitionName(view, name)
        }
    }

    open fun loadData() {}

    open fun onNavigationIconClicked() {
        findNavController().navigateUp()
    }
}