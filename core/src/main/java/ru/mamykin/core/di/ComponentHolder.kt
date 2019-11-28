package ru.mamykin.core.di

import ru.mamykin.core.di.component.AppComponent

interface ComponentHolder {

    fun appComponent(): AppComponent

    fun dependenciesProvider(): DependenciesProvider
}