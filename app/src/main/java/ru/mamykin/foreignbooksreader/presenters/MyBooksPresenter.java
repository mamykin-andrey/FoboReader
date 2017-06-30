package ru.mamykin.foreignbooksreader.presenters;

import android.text.TextUtils;

import com.arellomobile.mvp.InjectViewState;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import ru.mamykin.foreignbooksreader.ReaderApp;
import ru.mamykin.foreignbooksreader.database.BookDao;
import ru.mamykin.foreignbooksreader.events.UpdateEvent;
import ru.mamykin.foreignbooksreader.models.FictionBook;
import ru.mamykin.foreignbooksreader.views.MyBooksView;

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@InjectViewState
public class MyBooksPresenter extends BasePresenter<MyBooksView> {
    private List<FictionBook> booksList;
    private String searchQuery;
    private BookDao.SortOrder sortOrder;
    @Inject
    protected BookDao bookDao;

    public MyBooksPresenter() {
        ReaderApp.getComponent().inject(this);
    }

    @Override
    public void attachView(MyBooksView view) {
        super.attachView(view);

        loadBooksList();

        EventBus.getDefault().register(this);
        onMessageEvent(EventBus.getDefault().getStickyEvent(UpdateEvent.class));
    }

    @Subscribe
    public void onMessageEvent(UpdateEvent e) {
        if (e != null) {
            EventBus.getDefault().removeStickyEvent(e.getClass());
            loadBooksList();
        }
    }

    @Override
    public void detachView(MyBooksView view) {
        super.detachView(view);

        EventBus.getDefault().unregister(this);
    }

    public void onActionSortNameSelected() {
        sortOrder = BookDao.SortOrder.BY_NAME;
        loadBooksList();
    }

    public void onActionSortReadedSelected() {
        sortOrder = BookDao.SortOrder.BY_READED;
        loadBooksList();
    }

    public void onActionSortDateSelected() {
        sortOrder = BookDao.SortOrder.BY_DATE;
        loadBooksList();
    }

    public void onBookClicked(int position) {
        getViewState().openBook(booksList.get(position).getId());
    }

    public void onBookAboutClicked(int position) {
        getViewState().openBookDetails(booksList.get(position).getId());
    }

    public void onBookShareClicked(int position) {
        final FictionBook book = booksList.get(position);
        if (TextUtils.isEmpty(book.getDocUrl())) {
            getViewState().showBookShareDialog(book.getBookTitle());
        } else {
            getViewState().showBookShareDialog(book.getBookTitle(), book.getDocUrl());
        }
    }

    public void onBookRemoveClicked(int position) {
        bookDao.delete(booksList.get(position));
        loadBooksList();
    }

    public void onQueryTextChange(String text) {
        searchQuery = text;
        loadBooksList();
    }

    public void loadBooksList() {
        booksList = bookDao.getBooksList(searchQuery, sortOrder);
        if (booksList.size() == 0) {
            getViewState().showEmptyStateView();
        } else {
            getViewState().showBooksList(booksList);
        }
    }
}