package ru.mamykin.foreignbooksreader.ui.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.mamykin.foreignbooksreader.R;
import ru.mamykin.foreignbooksreader.ReaderApp;
import ru.mamykin.foreignbooksreader.common.UiUtils;
import ru.mamykin.foreignbooksreader.models.StoreBook;
import ru.mamykin.foreignbooksreader.ui.adapters.viewholders.StoreBookViewHolder;

/**
 * Адаптер с книгами на странице "Магазин"
 */
public class BooksStoreRecyclerAdapter extends RecyclerView.Adapter<StoreBookViewHolder> {
    private List<StoreBook> booksList;
    @Inject
    protected Context context;

    public BooksStoreRecyclerAdapter() {
        booksList = new ArrayList<>();
        ReaderApp.getComponent().inject(this);
    }

    @Override
    public StoreBookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_store_book, parent, false);
        return new StoreBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StoreBookViewHolder holder, int position) {
        StoreBook book = getItem(position);
        holder.tvBookName.setText(book.getTitle());
        holder.tvBookAuthor.setText(book.getAuthor());
        holder.tvBookCategory.setText(book.getGenre());
        holder.tvRating.setText(book.getRatingStr());
        holder.tvBookPrice.setText(book.getPrice());
        holder.tvBookOldPrice.setText(book.getOldPrice());
        UiUtils.setVisibility(holder.tvBookOldPrice, book.getOldPrice() != null);
        holder.tvBookOldPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        Picasso.with(context).load(book.getPictureUrl()).placeholder(R.drawable.img_no_image).into(holder.ivCover);
    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }

    @NonNull
    public StoreBook getItem(int position) {
        return booksList.get(position);
    }

    public void changeData(List<StoreBook> booksList) {
        this.booksList = booksList;
        notifyDataSetChanged();
    }
}