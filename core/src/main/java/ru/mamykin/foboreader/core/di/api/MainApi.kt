package ru.mamykin.foboreader.core.di.api

import ru.mamykin.foboreader.core.navigation.TabComposableProvider

interface MainApi {

    fun tabComposableProvider(): TabComposableProvider
}