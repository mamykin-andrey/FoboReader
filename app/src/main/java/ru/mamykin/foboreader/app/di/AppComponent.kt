package ru.mamykin.foboreader.app.di

import android.content.Context
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import dagger.BindsInstance
import dagger.Component
import ru.mamykin.foboreader.book_details.di.BookDetailsComponent
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.di.api.NavigationApi
import ru.mamykin.foboreader.core.di.api.NetworkApi
import ru.mamykin.foboreader.core.di.api.SettingsApi
import ru.mamykin.foboreader.my_books.di.MyBooksComponent
import ru.mamykin.foboreader.read_book.di.ReadBookComponent
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent : NavigationApi, NetworkApi, CommonApi, SettingsApi {

    fun bookDetailsComponentFactory(): BookDetailsComponent.Factory

    fun myBooksComponent(): MyBooksComponent

    fun readBookComponentFactory(): ReadBookComponent.Factory

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context,
            @BindsInstance cicerone: Cicerone<Router>
        ): AppComponent
    }
}