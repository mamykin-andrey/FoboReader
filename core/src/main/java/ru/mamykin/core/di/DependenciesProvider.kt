package ru.mamykin.core.di

import androidx.lifecycle.ViewModelProvider

interface DependenciesProvider {

    fun viewModelFactory(): ViewModelProvider.Factory
}