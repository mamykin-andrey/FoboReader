package ru.mamykin.foreignbooksreader.ui.adapters.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.mamykin.foreignbooksreader.R;

public class FileViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tvFileName)
    public TextView tvFileName;
    @BindView(R.id.tvFileAttributes)
    public TextView tvFileAttributes;
    @BindView(R.id.ivFileType)
    public ImageView ivFileType;
    @BindView(R.id.pbLoading)
    public View pbLoading;

    public FileViewHolder(View itemView, OnItemClickListener listener) {
        super(itemView);

        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(v -> listener.onItemClick(getAdapterPosition()));
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}