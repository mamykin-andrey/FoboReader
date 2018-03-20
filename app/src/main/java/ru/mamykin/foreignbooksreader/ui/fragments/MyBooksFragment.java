package ru.mamykin.foreignbooksreader.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.mamykin.foreignbooksreader.R;
import ru.mamykin.foreignbooksreader.common.UiUtils;
import ru.mamykin.foreignbooksreader.models.FictionBook;
import ru.mamykin.foreignbooksreader.presenters.MyBooksPresenter;
import ru.mamykin.foreignbooksreader.ui.activities.BookDetailsActivity;
import ru.mamykin.foreignbooksreader.ui.activities.ReadBookActivity;
import ru.mamykin.foreignbooksreader.ui.adapters.BooksRecyclerAdapter;
import ru.mamykin.foreignbooksreader.views.MyBooksView;

/**
 * Страница с книгами пользователя
 */
public class MyBooksFragment extends MvpAppCompatFragment implements SearchView.OnQueryTextListener,
        MyBooksView, BooksRecyclerAdapter.OnBookClickListener {
    @BindView(R.id.rvBooks)
    protected RecyclerView rvBooks;
    @BindView(R.id.vNoBooks)
    protected View vNoBooks;

    private BooksRecyclerAdapter adapter;

    @InjectPresenter
    MyBooksPresenter presenter;

    public static MyBooksFragment newInstance() {
        Bundle args = new Bundle();
        MyBooksFragment fragment = new MyBooksFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View contentView = inflater.inflate(R.layout.fragment_my_books, container, false);
        ButterKnife.bind(this, contentView);

        adapter = new BooksRecyclerAdapter(this);
        UiUtils.setupRecyclerView(getContext(), rvBooks,
                adapter, new LinearLayoutManager(getContext()), false);

        return contentView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        UiUtils.setupSearchView(getContext(), menu, R.id.action_search, R.string.menu_search, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_books_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionSortName:
                presenter.onActionSortNameSelected();
                return true;
            case R.id.actionSortReaded:
                presenter.onActionSortReadedSelected();
                return true;
            case R.id.actionSortDate:
                presenter.onActionSortDateSelected();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBookClicked(int position) {
        presenter.onBookClicked(position);
    }

    @Override
    public void onBookAboutClicked(int position) {
        presenter.onBookAboutClicked(position);
    }

    @Override
    public void onBookShareClicked(int position) {
        presenter.onBookShareClicked(position);
    }

    @Override
    public void onBookRemoveClicked(int position) {
        presenter.onBookRemoveClicked(position);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        presenter.onQueryTextChange(newText);
        return true;
    }

    @Override
    public void showEmptyStateView() {
        vNoBooks.setVisibility(View.VISIBLE);
        rvBooks.setVisibility(View.GONE);
    }

    @Override
    public void showBooksList(List<FictionBook> booksList) {
        vNoBooks.setVisibility(View.GONE);
        rvBooks.setVisibility(View.VISIBLE);
        adapter.changeData(booksList);
    }

    @Override
    public void openBook(int bookId) {
        startActivity(ReadBookActivity.Companion.getStartIntent(getContext(), bookId));
    }

    @Override
    public void openBookDetails(int bookId) {
        startActivity(BookDetailsActivity.Companion.getStartIntent(getContext(), bookId));
    }

    @Override
    public void showBookShareDialog(String title, String url) {
        showBookShareDialog(getString(R.string.download_on, title, url));
    }

    @Override
    public void showBookShareDialog(String title) {
        final Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, title);
        shareIntent.setType("text/plain");
        startActivity(shareIntent);
    }
}