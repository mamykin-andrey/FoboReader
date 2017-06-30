package ru.mamykin.foreignbooksreader.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.mamykin.foreignbooksreader.R;
import ru.mamykin.foreignbooksreader.ReaderApp;
import ru.mamykin.foreignbooksreader.models.AndroidFile;
import ru.mamykin.foreignbooksreader.ui.adapters.viewholders.FileViewHolder;

/**
 * Адаптер с файлами на странице "Устройство"
 */
public class FilesRecyclerAdapter extends RecyclerView.Adapter<FileViewHolder> {
    private FileViewHolder.OnItemClickListener listener;
    private List<AndroidFile> filesList;
    @Inject
    protected Context context;

    public FilesRecyclerAdapter(FileViewHolder.OnItemClickListener listener) {
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
        final AndroidFile file = getItem(position);
        holder.tvFileName.setText(file.getName());
        if (file.isDirectory()) {
            holder.ivFileType.setImageResource(R.drawable.ic_folder);
            holder.tvFileAttributes.setVisibility(View.GONE);
        } else if (file.isFictionBook()) {
            holder.ivFileType.setImageResource(R.drawable.ic_book);
            holder.tvFileAttributes.setVisibility(View.VISIBLE);
            holder.tvFileAttributes.setText(file.getAttributes());
        } else {
            holder.ivFileType.setImageResource(R.drawable.ic_file);
            holder.tvFileAttributes.setVisibility(View.VISIBLE);
            holder.tvFileAttributes.setText(file.getAttributes());
        }
    }

    @NonNull
    public AndroidFile getItem(int position) {
        return filesList.get(position);
    }

    @Override
    public int getItemCount() {
        return filesList.size();
    }

    public void changeData(List<AndroidFile> filesList) {
        this.filesList = filesList;
        notifyDataSetChanged();
    }
}