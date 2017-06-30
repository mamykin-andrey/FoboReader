package ru.mamykin.foreignbooksreader.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.mamykin.foreignbooksreader.R;
import ru.mamykin.foreignbooksreader.common.UiUtils;
import ru.mamykin.foreignbooksreader.models.StoreBook;
import ru.mamykin.foreignbooksreader.presenters.StorePresenter;
import ru.mamykin.foreignbooksreader.ui.adapters.BooksStoreRecyclerAdapter;
import ru.mamykin.foreignbooksreader.views.BooksStoreView;

/**
 * Страница с магазином книг
 */
public class BooksStoreFragment extends MvpAppCompatFragment implements BooksStoreView, SearchView.OnQueryTextListener {
    @InjectPresenter
    StorePresenter presenter;

    private BooksStoreRecyclerAdapter adapter;

    @BindView(R.id.rvBooks)
    protected RecyclerView rvBooks;
    @BindView(R.id.srlRefresh)
    protected SwipeRefreshLayout srlRefresh;

    public static BooksStoreFragment newInstance() {
        Bundle args = new Bundle();
        BooksStoreFragment fragment = new BooksStoreFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View contentView = inflater.inflate(R.layout.fragment_main_store, container, false);
        ButterKnife.bind(this, contentView);
        srlRefresh.setOnRefreshListener(() -> presenter.loadStoreCategories());

        adapter = new BooksStoreRecyclerAdapter();
        UiUtils.setupRecyclerView(getContext(), rvBooks,
                adapter, new LinearLayoutManager(getContext()), false);

        initCategoriesRecyclerView();
        return contentView;
    }

    private void initCategoriesRecyclerView() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_books_store, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        UiUtils.setupSearchView(getContext(), menu, R.id.action_search, R.string.menu_search, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showBooks(List<StoreBook> booksList) {
        adapter.changeData(booksList);
    }

    @Override
    public void showMessage(String message) {
        UiUtils.showToast(getContext(), message, Toast.LENGTH_SHORT);
    }

    @Override
    public void showLoading(boolean show) {
        srlRefresh.setRefreshing(show);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}