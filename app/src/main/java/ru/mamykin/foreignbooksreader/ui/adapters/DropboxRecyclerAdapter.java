package ru.mamykin.foreignbooksreader.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.mamykin.foreignbooksreader.R;
import ru.mamykin.foreignbooksreader.ReaderApp;
import ru.mamykin.foreignbooksreader.models.DropboxFile;
import ru.mamykin.foreignbooksreader.ui.adapters.viewholders.FileViewHolder;

/**
 * Адаптер с файлами на странице "Dropbox"
 */
public class DropboxRecyclerAdapter extends RecyclerView.Adapter<FileViewHolder> {
    private FileViewHolder.OnItemClickListener listener;
    private List<DropboxFile> filesList = new ArrayList<>(0);
    @Inject
    protected Context context;
    private int loadingItem = -1;

    public DropboxRecyclerAdapter(FileViewHolder.OnItemClickListener listener) {
        this.listener = listener;
        filesList = new ArrayList<>();
        ReaderApp.getComponent().inject(this);
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_file, parent, false);
        return new FileViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(FileViewHolder holder, int position) {
        final DropboxFile item = getItem(position);
        if (position == loadingItem) {
            // Загружаемый элемент
            holder.pbLoading.setVisibility(View.VISIBLE);
        } else {
            holder.tvFileName.setText(item.getName());
            holder.pbLoading.setVisibility(View.GONE);
            if (item.isDirectory()) {
                // Папка
                holder.ivFileType.setImageResource(R.drawable.ic_folder);
                holder.tvFileAttributes.setVisibility(View.GONE);
            } else if (item.isFictionBook()) {
                // Fiction Book
                holder.ivFileType.setImageResource(R.drawable.ic_book);
                holder.tvFileAttributes.setVisibility(View.VISIBLE);
                holder.tvFileAttributes.setText(item.getAttributes());
            } else {
                // Обычный файл
                holder.ivFileType.setImageResource(R.drawable.ic_file);
                holder.tvFileAttributes.setVisibility(View.VISIBLE);
                holder.tvFileAttributes.setText(item.getAttributes());
            }
        }
    }

    @NonNull
    public DropboxFile getItem(int position) {
        return filesList.get(position);
    }

    public int getItemCount() {
        return filesList.size();
    }

    public void changeData(List<DropboxFile> files) {
        filesList = files;
        notifyDataSetChanged();
    }

    public void showLoadingItem(int position) {
        this.loadingItem = position;
        notifyDataSetChanged();
    }

    public void hideLoadingItem() {
        this.loadingItem = -1;
        notifyDataSetChanged();
    }
}