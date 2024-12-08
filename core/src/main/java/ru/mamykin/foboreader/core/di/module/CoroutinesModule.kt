package ru.mamykin.foboreader.core.di.module

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
class CoroutinesModule {

    @Provides
    fun provideRealCoroutineScope(): CoroutineScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
}