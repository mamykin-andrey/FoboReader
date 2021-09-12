package ru.mamykin.foboreader.core.di.api

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import ru.mamykin.foboreader.core.navigation.ScreenProvider

interface NavigationApi {

    fun getRouter(): Router

    fun getCicerone(): Cicerone<Router>

    fun getScreenProvider(): ScreenProvider
}