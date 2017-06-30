package ru.mamykin.foreignbooksreader.di;

import javax.inject.Singleton;

import dagger.Component;
import ru.mamykin.foreignbooksreader.ReaderApp;
import ru.mamykin.foreignbooksreader.di.modules.ApiModule;
import ru.mamykin.foreignbooksreader.di.modules.AppModule;
import ru.mamykin.foreignbooksreader.di.modules.DatabaseModule;
import ru.mamykin.foreignbooksreader.di.modules.MappersModule;
import ru.mamykin.foreignbooksreader.di.modules.PreferencesModule;
import ru.mamykin.foreignbooksreader.presenters.AboutPresenter;
import ru.mamykin.foreignbooksreader.presenters.BookDetailsPresenter;
import ru.mamykin.foreignbooksreader.presenters.DeviceBooksPresenter;
import ru.mamykin.foreignbooksreader.presenters.DropboxBooksPresenter;
import ru.mamykin.foreignbooksreader.presenters.MainPresenter;
import ru.mamykin.foreignbooksreader.presenters.StorePresenter;
import ru.mamykin.foreignbooksreader.presenters.MyBooksPresenter;
import ru.mamykin.foreignbooksreader.presenters.ReadBookPresenter;
import ru.mamykin.foreignbooksreader.presenters.SettingsPresenter;
import ru.mamykin.foreignbooksreader.ui.activities.BaseActivity;
import ru.mamykin.foreignbooksreader.ui.adapters.BooksRecyclerAdapter;
import ru.mamykin.foreignbooksreader.ui.adapters.BooksStoreRecyclerAdapter;
import ru.mamykin.foreignbooksreader.ui.adapters.DropboxRecyclerAdapter;
import ru.mamykin.foreignbooksreader.ui.adapters.FilesRecyclerAdapter;
import ru.mamykin.foreignbooksreader.ui.adapters.PromotionsRecyclerAdapter;
import ru.mamykin.foreignbooksreader.ui.fragments.MyBooksFragment;
import ru.mamykin.foreignbooksreader.preferences.PreferencesManager;

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@Component(modules = {AppModule.class, ApiModule.class, DatabaseModule.class, PreferencesModule.class, MappersModule.class})
@Singleton
public interface AppComponent {
    void inject(MyBooksFragment fragment);

    void inject(BaseActivity activity);

    void inject(FilesRecyclerAdapter adapter);

    void inject(DropboxRecyclerAdapter adapter);

    void inject(BooksRecyclerAdapter adapter);

    void inject(BooksStoreRecyclerAdapter adapter);

    void inject(PromotionsRecyclerAdapter adapter);

    void inject(PreferencesManager manager);

    void inject(BookDetailsPresenter presenter);

    void inject(DropboxBooksPresenter presenter);

    void inject(MyBooksPresenter presenter);

    void inject(SettingsPresenter presenter);

    void inject(ReadBookPresenter presenter);

    void inject(StorePresenter presenter);

    void inject(AboutPresenter presenter);

    void inject(DeviceBooksPresenter presenter);

    void inject(MainPresenter presenter);

    void inject(ReaderApp application);
}