package ru.mamykin.foboreader.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import ru.mamykin.foboreader.ReaderApp
import ru.mamykin.foboreader.core.di.component.AppComponent
import javax.inject.Inject

abstract class BaseFragment : AndroidXMvpFragment(), BaseView {

    abstract val layoutId: Int

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getAppComponent().inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutId, container, false)

    override fun onError(message: String) {
        showSnackbar(message, false)
    }

    override fun showLoading(show: Boolean) {}

    protected fun getAppComponent(): AppComponent =
            (activity!!.application as ReaderApp).appComponent

    inline fun <reified T : ViewModel> viewModel(): Lazy<T> = lazy { getViewModel<T>() }

    inline fun <reified T : ViewModel> getViewModel(): T =
            ViewModelProviders.of(this, viewModelFactory)[T::class.java]

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