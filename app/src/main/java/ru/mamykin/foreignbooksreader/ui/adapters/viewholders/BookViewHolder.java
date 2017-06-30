package ru.mamykin.foreignbooksreader.ui.adapters.viewholders;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.mamykin.foreignbooksreader.R;
import ru.mamykin.foreignbooksreader.ui.adapters.BooksRecyclerAdapter;
import ru.mamykin.foreignbooksreader.ui.controls.PercentProgressView;

public class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.tvBookTitle)
    public TextView tvBookTitle;
    @BindView(R.id.tvAuthor)
    public TextView tvBookAuthor;
    @BindView(R.id.tvBooksPages)
    public TextView tvBookPages;
    @BindView(R.id.tvBookAddedDate)
    public TextView tvBookAddedDate;
    @BindView(R.id.pvProgress)
    public PercentProgressView vProgress;
    @BindView(R.id.ivBookCover)
    public ImageView ivBookCover;
    @BindView(R.id.btnMenu)
    public View btnMenu;
    @BindView(R.id.tvFormat)
    public View vFormat;

    private BooksRecyclerAdapter.OnBookClickListener listener;

    public BookViewHolder(View itemView, BooksRecyclerAdapter.OnBookClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.listener = listener;
        itemView.setOnClickListener(this);
        btnMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnMenu) {
            final PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.setOnMenuItemClickListener(menuListener);
            popupMenu.inflate(R.menu.menu_book_item);
            popupMenu.show();
        } else {
            listener.onBookClicked(getAdapterPosition());
        }
    }

    private PopupMenu.OnMenuItemClickListener menuListener = item -> {
        switch (item.getItemId()) {
            case R.id.menu_about_book:
                listener.onBookAboutClicked(getAdapterPosition());
                break;
            case R.id.menu_share_book:
                listener.onBookShareClicked(getAdapterPosition());
                break;
            case R.id.menu_remove_book:
                listener.onBookRemoveClicked(getAdapterPosition());
                break;
        }
        return true;
    };
}