package ru.mamykin.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import ru.mamykin.core.R

abstract class BaseFragment(
        @LayoutRes private val layoutId: Int
) : Fragment() {

    protected var toolbar: Toolbar? = null

    private var dataLoaded = false

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layoutId, container, false)
        toolbar = view.findViewById(R.id.toolbar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!dataLoaded) {
            dataLoaded = true
            loadData()
        }
    }

    open fun loadData() {}
}