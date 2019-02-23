package ru.mamykin.foboreader.core.di.modules

import dagger.Module
import dagger.Provides
import ru.mamykin.foboreader.core.di.scope.FragmentScope
import ru.mamykin.foboreader.presentation.devicebooks.DeviceBooksRouter

@Module
@FragmentScope
class DeviceBooksModule(
        private val router: DeviceBooksRouter
) {

    @Provides
    @FragmentScope
    fun provideRouter() = router
}