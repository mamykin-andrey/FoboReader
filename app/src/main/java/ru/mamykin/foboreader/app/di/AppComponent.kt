package ru.mamykin.foboreader.app.di

import android.content.Context
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import dagger.BindsInstance
import dagger.Component
import ru.mamykin.foboreader.book_details.di.BookDetailsComponent
import ru.mamykin.foboreader.my_books.di.MyBooksComponent
import ru.mamykin.foboreader.read_book.di.ReadBookComponent
import ru.mamykin.foboreader.settings.di.SettingsComponent
import ru.mamykin.foboreader.store.di.BooksStoreComponent
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun mainComponent(): MainComponent

    fun booksStoreComponent(): BooksStoreComponent

    fun settingsComponent(): SettingsComponent

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