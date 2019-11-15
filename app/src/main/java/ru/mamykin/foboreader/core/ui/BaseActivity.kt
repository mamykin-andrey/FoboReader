package ru.mamykin.foboreader.core.ui

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.ReaderApp
import ru.mamykin.foboreader.core.di.component.AppComponent
import javax.inject.Inject

abstract class BaseActivity(
        @LayoutRes private val layoutId: Int
) : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    inline fun <reified T : ViewModel> viewModel(): Lazy<T> = lazy { getViewModel<T>() }

    inline fun <reified T : ViewModel> getViewModel(): T =
            ViewModelProviders.of(this, viewModelFactory)[T::class.java]

    override fun onCreate(savedInstanceState: Bundle?) {
        getAppComponent().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
    }

    protected fun initToolbar(title: String, homeEnabled: Boolean) {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(homeEnabled)
    }

    protected fun getAppComponent(): AppComponent = (application as ReaderApp).appComponent
}