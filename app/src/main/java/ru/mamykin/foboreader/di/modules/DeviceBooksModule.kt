package ru.mamykin.foboreader.di.modules

import dagger.Module
import dagger.Provides
import ru.mamykin.foboreader.di.scope.FragmentScope
import ru.mamykin.foboreader.ui.devicebooks.DeviceBooksRouter

@Module
@FragmentScope
class DeviceBooksModule(
        private val router: DeviceBooksRouter
) {

    @Provides
    @FragmentScope
    fun provideRouter() = router
}