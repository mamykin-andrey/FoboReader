package ru.mamykin.core.di.module

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

class KotlinViewModelProvider private constructor() {

    companion object {

        inline fun <reified T : ViewModel> of(
                fragment: Fragment,
                viewModel: ViewModel
        ): T {
            @Suppress("UNCHECKED_CAST")
            val vmFactory = object : ViewModelProvider.Factory {
                override fun <U : ViewModel> create(modelClass: Class<U>): U = viewModel as U
            }

            return ViewModelProviders.of(fragment, vmFactory)[T::class.java]
        }

        inline fun <reified T : ViewModel> of(
                activity: FragmentActivity,
                crossinline factory: () -> T
        ): T {
            @Suppress("UNCHECKED_CAST")
            val vmFactory = object : ViewModelProvider.Factory {
                override fun <U : ViewModel> create(modelClass: Class<U>): U = factory() as U
            }

            return ViewModelProviders.of(activity, vmFactory)[T::class.java]
        }
    }
}