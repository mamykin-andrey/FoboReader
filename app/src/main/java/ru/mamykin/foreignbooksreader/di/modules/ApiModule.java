package ru.mamykin.foreignbooksreader.di.modules;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mamykin.foreignbooksreader.retrofit.BooksStoreService;
import ru.mamykin.foreignbooksreader.retrofit.UpdateService;
import ru.mamykin.foreignbooksreader.retrofit.YandexTranslateService;

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@Module
public class ApiModule {
    @Provides
    public YandexTranslateService provideYandexTranslateService() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(YandexTranslateService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

            return retrofit.create(YandexTranslateService.class);
    }

    @Provides
    public BooksStoreService provideBooksStoreService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BooksStoreService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(BooksStoreService.class);
    }

    @Provides
    public UpdateService provideUpdateService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UpdateService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(UpdateService.class);
    }
}