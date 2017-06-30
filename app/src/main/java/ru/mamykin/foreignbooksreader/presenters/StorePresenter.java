package ru.mamykin.foreignbooksreader.presenters;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import ru.mamykin.foreignbooksreader.ReaderApp;
import ru.mamykin.foreignbooksreader.common.Utils;
import ru.mamykin.foreignbooksreader.models.StoreBook;
import ru.mamykin.foreignbooksreader.retrofit.BooksStoreService;
import ru.mamykin.foreignbooksreader.views.BooksStoreView;
import rx.Subscriber;
import rx.Subscription;

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@InjectViewState
public class StorePresenter extends BasePresenter<BooksStoreView> {
    private List<StoreBook> booksList;
    @Inject
    protected BooksStoreService booksService;

    public StorePresenter() {
        ReaderApp.getComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        loadStoreCategories();
    }

    public void loadStoreCategories() {
        getViewState().showLoading(true);
        Subscription subscription = booksService.getBooks()
                .compose(Utils.applySchedulers())
                .subscribe(new Subscriber<List<StoreBook>>() {
                    @Override
                    public void onCompleted() {
                        getViewState().showLoading(false);
                        getViewState().showBooks(booksList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        getViewState().showLoading(false);
                        getViewState().showMessage(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<StoreBook> booksList) {
                        StorePresenter.this.booksList = booksList;
                    }
                });
        unsubscribeOnDestroy(subscription);
    }
}