package ru.mamykin.foboreader.core.di.api

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import ru.mamykin.foboreader.core.navigation.LocalCiceroneHolder
import ru.mamykin.foboreader.core.navigation.ScreenProvider

interface NavigationApi {

    fun getCicerone(): Cicerone<Router>

    fun getScreenProvider(): ScreenProvider

    fun getLocalCiceroneHolder(): LocalCiceroneHolder
}