package ru.mamykin.core.di

interface ComponentHolder {

    fun dependenciesProvider(): DependenciesProvider
}