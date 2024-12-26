package ru.mamykin.foboreader.read_book.reader

import android.content.Context
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import ru.mamykin.foboreader.common_book_info.data.database.BookInfoDao
import ru.mamykin.foboreader.common_book_info.data.database.BookInfoDaoFactory
import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository
import ru.mamykin.foboreader.core.data.RetrofitServiceFactory
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.di.api.NetworkApi
import ru.mamykin.foboreader.core.di.api.SettingsApi
import ru.mamykin.foboreader.core.di.qualifier.CommonClient
import ru.mamykin.foboreader.read_book.BuildConfig
import ru.mamykin.foboreader.read_book.translation.GoogleTranslateService
import javax.inject.Named
import javax.inject.Scope

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
internal annotation class ReadBookScope

@ReadBookScope
@Component(
    dependencies = [NetworkApi::class, CommonApi::class, SettingsApi::class],
    modules = [ReadBookProvidesModule::class, ReadBookBindsModule::class]
)
internal interface ReadBookComponent {

    fun viewModel(): ReadBookViewModel

    fun vibrationManager(): VibrationManager

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance @Named("bookId") bookId: Long,
            networkApi: NetworkApi,
            commonApi: CommonApi,
            settingsApi: SettingsApi
        ): ReadBookComponent
    }
}

@Module
internal class ReadBookProvidesModule {

    @Provides
    @ReadBookScope
    fun provideGoogleTranslateService(@CommonClient client: OkHttpClient): GoogleTranslateService =
        RetrofitServiceFactory.create(
            client,
            GoogleTranslateService.BASE_URL
        )

    @Named("ApiKey")
    @Provides
    @ReadBookScope
    fun provideGoogleApiKey() = BuildConfig.googleApiKey

    @Named("ApiHost")
    @Provides
    @ReadBookScope
    fun provideGoogleApiHost() = BuildConfig.googleApiHost

    @Provides
    @ReadBookScope
    fun provideBookInfoRepository(dao: BookInfoDao): BookInfoRepository = BookInfoRepository(dao)

    @Provides
    @ReadBookScope
    fun provideBookInfoDao(context: Context): BookInfoDao = BookInfoDaoFactory.create(context)
}

@Module
internal interface ReadBookBindsModule {

    @Binds
    fun bindVibrationManager(impl: VibrationManagerImpl): VibrationManager
}