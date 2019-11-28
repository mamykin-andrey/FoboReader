package ru.mamykin.core.ui

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import ru.mamykin.core.R
import ru.mamykin.core.di.ComponentHolder
import ru.mamykin.core.di.component.AppComponent

abstract class BaseActivity(
        @LayoutRes private val layoutId: Int
) : AppCompatActivity() {

    val viewModelFactory: ViewModelProvider.Factory by lazy {
        (application as ComponentHolder)
                .dependenciesProvider()
                .viewModelFactory()
    }

    inline fun <reified T : ViewModel> viewModel(): Lazy<T> = lazy { getViewModel<T>() }

    inline fun <reified T : ViewModel> getViewModel(): T =
            ViewModelProviders.of(this, viewModelFactory)[T::class.java]

    protected fun appComponent() = (application as ComponentHolder).appComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
    }

    protected fun initToolbar(title: String, homeEnabled: Boolean) {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(homeEnabled)
    }
}