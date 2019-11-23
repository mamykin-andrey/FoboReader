package ru.mamykin.core.ui

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import ru.mamykin.core.di.DependenciesProvider

abstract class BaseActivity(
        @LayoutRes private val layoutId: Int
) : AppCompatActivity() {

    val viewModelFactory: ViewModelProvider.Factory by lazy {
        (application as DependenciesProvider).viewModelFactory()
    }

    inline fun <reified T : ViewModel> viewModel(): Lazy<T> = lazy { getViewModel<T>() }

    inline fun <reified T : ViewModel> getViewModel(): T =
            ViewModelProviders.of(this, viewModelFactory)[T::class.java]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
    }

//    protected fun initToolbar(title: String, homeEnabled: Boolean) {
//        val toolbar = findViewById<Toolbar>(R.id.toolbar)
//        setSupportActionBar(toolbar)
//        supportActionBar?.title = title
//        supportActionBar?.setDisplayHomeAsUpEnabled(homeEnabled)
//    }
}