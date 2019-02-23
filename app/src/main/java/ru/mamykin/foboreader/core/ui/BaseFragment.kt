package ru.mamykin.foboreader.core.ui

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.mamykin.foboreader.ReaderApp
import ru.mamykin.foboreader.core.di.component.AppComponent

abstract class BaseFragment : MvpAppCompatFragment(), BaseView {

    abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = inflater.inflate(layoutId, container, false)

    override fun onError(message: String) {
        showSnackbar(message, false)
    }

    protected open fun injectDependencies() {
    }

    protected fun getAppComponent(): AppComponent = (activity!!.application as ReaderApp).appComponent

    protected fun showToast(@StringRes messageResId: Int, long: Boolean = false) {
        showToast(getString(messageResId, long))
    }

    protected fun showToast(message: String, long: Boolean = false) {
        val duration = if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        Toast.makeText(context, message, duration).show()
    }

    protected fun showSnackbar(@StringRes messageResId: Int, long: Boolean = false) {
        showSnackbar(getString(messageResId, long))
    }

    protected fun showSnackbar(message: String, long: Boolean = false) {
        val duration = if (long) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT
        activity?.findViewById<View>(android.R.id.content)?.let {
            Snackbar.make(it, message, duration)
        }
    }
}