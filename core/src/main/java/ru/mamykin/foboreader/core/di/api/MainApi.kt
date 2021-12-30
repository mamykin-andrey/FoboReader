package ru.mamykin.foboreader.core.di.api

import ru.mamykin.foboreader.core.navigation.TabFragmentProvider

interface MainApi {

    fun tabFragmentProvider(): TabFragmentProvider
}