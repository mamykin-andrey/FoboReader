package ru.mamykin.foboreader.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.mamykin.foboreader.core.R

abstract class BaseFragment(
        @LayoutRes private val layoutId: Int
) : Fragment() {

    protected var toolbar: Toolbar? = null
    protected open val useNavigationIcon: Boolean
        get() = false

    private var dataLoaded = false

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layoutId, container, false)
        initToolbar(view)
        return view
    }

    private fun initToolbar(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        toolbar?.takeIf { useNavigationIcon }
                ?.setNavigationOnClickListener { onNavigationIconClicked() }
                ?: run { toolbar?.navigationIcon = null }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!dataLoaded) {
            dataLoaded = true
            loadData()
        }
    }

    open fun loadData() {}

    open fun onNavigationIconClicked() {
        findNavController().popBackStack()
    }
}