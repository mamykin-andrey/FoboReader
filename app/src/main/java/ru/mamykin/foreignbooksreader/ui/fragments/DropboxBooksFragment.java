package ru.mamykin.foreignbooksreader.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.mamykin.foreignbooksreader.R;
import ru.mamykin.foreignbooksreader.common.UiUtils;
import ru.mamykin.foreignbooksreader.models.DropboxFile;
import ru.mamykin.foreignbooksreader.presenters.DropboxBooksPresenter;
import ru.mamykin.foreignbooksreader.ui.activities.ReadBookActivity;
import ru.mamykin.foreignbooksreader.ui.adapters.DropboxRecyclerAdapter;
import ru.mamykin.foreignbooksreader.views.DropboxView;

/**
 * Страница с файлами Dropbox
 */
public class DropboxBooksFragment extends MvpAppCompatFragment implements DropboxView, SearchView.OnQueryTextListener {
    private static final String CURRENT_DIR_EXTRA = "url_path_extra";

    @BindView(R.id.tvCurrentDir)
    protected TextView tvCurrentDir;
    @BindView(R.id.pbLoading)
    protected View pbLoading;
    @BindView(R.id.llFiles)
    protected View llFiles;
    @BindView(R.id.llNoAuth)
    protected View llNoAuth;
    @BindView(R.id.rvBooks)
    protected RecyclerView rvBooks;
    @BindView(R.id.btnLogin)
    protected View btnLogin;
    @BindView(R.id.ibUp)
    protected View btnUpDir;

    @InjectPresenter
    DropboxBooksPresenter presenter;

    private DropboxRecyclerAdapter adapter;

    public static DropboxBooksFragment newInstance(String currentDir) {
        Bundle args = new Bundle();
        args.putString(CURRENT_DIR_EXTRA, currentDir == null ? "" : currentDir);
        DropboxBooksFragment fragment = new DropboxBooksFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @ProvidePresenter
    DropboxBooksPresenter provideDropboxBooksPresenter() {
        return new DropboxBooksPresenter(getArguments().getString(CURRENT_DIR_EXTRA));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View contentView = inflater.inflate(R.layout.fragment_dropbox_books, container, false);
        ButterKnife.bind(this, contentView);

        adapter = new DropboxRecyclerAdapter(position -> presenter.onItemClicked(position, adapter.getItem(position)));
        UiUtils.setupRecyclerView(getContext(), rvBooks, adapter, new LinearLayoutManager(getContext()), true);

        return contentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_dropbox_books, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        UiUtils.setupSearchView(getContext(), menu, R.id.action_search, R.string.menu_search, this);
    }

    @Override
    public void showFiles(List<DropboxFile> filesList) {
        rvBooks.setVisibility(View.VISIBLE);
        adapter.changeData(filesList);
    }

    @Override
    public void hideFiles() {
        rvBooks.setVisibility(View.GONE);
    }

    @Override
    public void showCurrentDir(String currentDir) {
        tvCurrentDir.setText(currentDir);
    }

    @Override
    public void openBook(String path) {
        startActivity(ReadBookActivity.getStartIntent(getContext(), path));
    }

    @Override
    public void showLoadingItem(int position) {
        adapter.showLoadingItem(position);
    }

    @Override
    public void hideLoadingItem() {
        adapter.hideLoadingItem();
    }

    @Override
    public void showLoading() {
        pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        pbLoading.setVisibility(View.GONE);
    }

    @Override
    public void showAuth() {
        llNoAuth.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAuth() {
        llNoAuth.setVisibility(View.GONE);
    }

    @Override
    public void showUpButton(boolean show) {
        UiUtils.setVisibility(btnUpDir, show);
    }

    @OnClick(R.id.btnLogin)
    public void onLoginClicked() {
        presenter.onLoginClicked();
    }

    @OnClick(R.id.ibUp)
    public void onUpClicked() {
        presenter.onUpClicked();
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