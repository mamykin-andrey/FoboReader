package ru.mamykin.foreignbooksreader.ui.adapters.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.mamykin.foreignbooksreader.R;

public class StoreBookViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tvName)
    public TextView tvBookName;
    @BindView(R.id.tvAuthor)
    public TextView tvBookAuthor;
    @BindView(R.id.tvCategory)
    public TextView tvBookCategory;
    @BindView(R.id.tvPrice)
    public TextView tvBookPrice;
    @BindView(R.id.tvOldPrice)
    public TextView tvBookOldPrice;
    @BindView(R.id.tvRating)
    public TextView tvRating;
    @BindView(R.id.ivCover)
    public ImageView ivCover;

    public StoreBookViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}