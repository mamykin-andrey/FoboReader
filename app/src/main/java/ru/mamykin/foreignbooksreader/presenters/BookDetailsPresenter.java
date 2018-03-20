package ru.mamykin.foreignbooksreader.presenters;

import com.arellomobile.mvp.InjectViewState;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import ru.mamykin.foreignbooksreader.ReaderApp;
import ru.mamykin.foreignbooksreader.common.UiUtils;
import ru.mamykin.foreignbooksreader.common.Utils;
import ru.mamykin.foreignbooksreader.database.BookDatabaseHelper;
import ru.mamykin.foreignbooksreader.models.FictionBook;
import ru.mamykin.foreignbooksreader.views.BookDetailsView;

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@InjectViewState
public class BookDetailsPresenter extends BasePresenter<BookDetailsView> {
    private FictionBook book;
    private int bookId;

    @Inject
    protected BookDatabaseHelper dbHelper;

    public BookDetailsPresenter(int bookId) {
        this.bookId = bookId;
        ReaderApp.getComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        loadBook();
    }

    private void loadBook() {
        book = dbHelper.getBookDao().getBook(bookId);

        getViewState().showBookName(book.getBookTitle());
        getViewState().showBookAuthor(book.getBookAuthor());
        getViewState().showBookPath(getFileFromPath(book.getFilePath()));
        getViewState().showBookCurrentPage(String.valueOf(book.getCurrentPage()));
        getViewState().showBookGenre(book.getBookGenre());
        getViewState().showBookOriginalLang(book.getBookSrcLang());
        getViewState().showBookCreatedDate(Utils.INSTANCE.getFormattedDate(book.getDocDate()));
    }

    private String getFileFromPath(String filePath) {
        return filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
    }

    public void onReadClicked() {
        getViewState().openBook(book.getId());
    }
}