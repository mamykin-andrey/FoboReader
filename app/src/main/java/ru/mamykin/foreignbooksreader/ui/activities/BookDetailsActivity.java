package ru.mamykin.foreignbooksreader.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import butterknife.BindView;
import butterknife.OnClick;
import ru.mamykin.foreignbooksreader.R;
import ru.mamykin.foreignbooksreader.common.UiUtils;
import ru.mamykin.foreignbooksreader.presenters.BookDetailsPresenter;
import ru.mamykin.foreignbooksreader.views.BookDetailsView;

/**
 * Страница с информацией о книге
 */
public class BookDetailsActivity extends BaseActivity implements BookDetailsView {
    private static final String BOOK_ID_EXTRA = "book_id_extra";

    @BindView(R.id.tvName)
    protected TextView tvBookName;
    @BindView(R.id.fabRead)
    protected FloatingActionButton fabRead;
    @BindView(R.id.tvAuthor)
    protected TextView tvBookAuthor;
    @BindView(R.id.tvBookPath)
    protected TextView tvBookPath;
    @BindView(R.id.tvBookCurrentPage)
    protected TextView tvBookCurrentPage;
    @BindView(R.id.tvBookGenre)
    protected TextView tvBookGenre;
    @BindView(R.id.tvBookLanguage)
    protected TextView tvBookLanguage;
    @BindView(R.id.tvBookCreatedDate)
    protected TextView tvBookCreatedDate;

    @InjectPresenter
    BookDetailsPresenter presenter;

    @ProvidePresenter
    BookDetailsPresenter provideBookDetailsPresenter() {
        return new BookDetailsPresenter(getIntent().getExtras().getInt(BOOK_ID_EXTRA));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar(getString(R.string.about_book), true);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_book_detail;
    }

    @Override
    public void showTitle(String title) {
        UiUtils.setTitle(this, title);
    }

    @Override
    public void setHomeEnabled(boolean enabled) {
        UiUtils.setHomeEnabled(this, enabled);
    }

    @Override
    public void showBookName(String name) {
        tvBookName.setText(name);
    }

    @Override
    public void showBookAuthor(String author) {
        tvBookAuthor.setText(author);
    }

    @Override
    public void showBookPath(String path) {
        tvBookPath.setText(path);
    }

    @Override
    public void showBookCurrentPage(String currentPage) {
        tvBookCurrentPage.setText(currentPage);
    }

    @Override
    public void showBookGenre(String genre) {
        tvBookGenre.setText(genre);
    }

    @Override
    public void showBookOriginalLang(String lang) {
        tvBookLanguage.setText(lang);
    }

    @Override
    public void showBookCreatedDate(String date) {
        tvBookCreatedDate.setText(date);
    }

    @Override
    public void openBook(int bookId) {
        startActivity(ReadBookActivity.getStartIntent(this, bookId));
    }

    @OnClick(R.id.fabRead)
    public void onReadClicked() {
        presenter.onReadClicked();
    }

    public static Intent getStartIntent(Context context, int bookId) {
        final Intent bookDetailsIntent = new Intent(context, BookDetailsActivity.class);
        bookDetailsIntent.putExtra(BookDetailsActivity.BOOK_ID_EXTRA, bookId);
        return bookDetailsIntent;
    }
}