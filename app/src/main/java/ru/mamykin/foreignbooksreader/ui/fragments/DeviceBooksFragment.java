package ru.mamykin.foreignbooksreader.ui.fragments;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.mamykin.foreignbooksreader.R;
import ru.mamykin.foreignbooksreader.common.UiUtils;
import ru.mamykin.foreignbooksreader.models.AndroidFile;
import ru.mamykin.foreignbooksreader.presenters.DeviceBooksPresenter;
import ru.mamykin.foreignbooksreader.ui.activities.ReadBookActivity;
import ru.mamykin.foreignbooksreader.ui.adapters.FilesRecyclerAdapter;
import ru.mamykin.foreignbooksreader.views.DeviceBooksView;

/**
 * Страница с файлами на устройстве
 */
public class DeviceBooksFragment extends MvpAppCompatFragment implements DeviceBooksView, SearchView.OnQueryTextListener {
    private static final String CURRENT_DIR_EXTRA = "current_dir_extra";

    @BindView(R.id.rvFiles)
    protected RecyclerView rvFiles;
    @BindView(R.id.tvCurrentDir)
    protected TextView tvCurrentDir;
    @BindView(R.id.ibUp)
    protected View btnUpDir;

    @InjectPresenter
    DeviceBooksPresenter presenter;

    private FilesRecyclerAdapter adapter;

    public static DeviceBooksFragment newInstance(String currentDir) {
        Bundle args = new Bundle();
        args.putString(CURRENT_DIR_EXTRA, currentDir == null ?
                Environment.getExternalStorageDirectory().toString() : currentDir);
        DeviceBooksFragment fragment = new DeviceBooksFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @ProvidePresenter
    DeviceBooksPresenter provideDeviceBooksPresenter() {
        return new DeviceBooksPresenter(getArguments().getString(CURRENT_DIR_EXTRA));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View contentView = inflater.inflate(R.layout.fragment_device_books, container, false);
        ButterKnife.bind(this, contentView);

        adapter = new FilesRecyclerAdapter(position -> presenter.onFileClicked(adapter.getItem(position)));
        UiUtils.setupRecyclerView(getContext(), rvFiles, adapter, new LinearLayoutManager(getContext()), true);

        return contentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_device_books, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        UiUtils.setupSearchView(getContext(), menu, R.id.action_search, R.string.menu_search, this);
    }

    @Override
    public void showFiles(List<AndroidFile> files) {
        adapter.changeData(files);
    }

    @Override
    public void setCurrentDir(String currentDir) {
        tvCurrentDir.setText(currentDir);
    }

    @Override
    public void showPermissionMessage() {
        UiUtils.showToast(getContext(), R.string.permission_denied);
    }

    @Override
    public void openBook(String path) {
        startActivity(ReadBookActivity.Companion.getStartIntent(getContext(), path));
    }

    @Override
    public void showUpDir(boolean show) {
        UiUtils.setVisibility(btnUpDir, show);
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