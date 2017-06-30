package ru.mamykin.foreignbooksreader.ui.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
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
import ru.mamykin.foreignbooksreader.common.UiUtils;
import ru.mamykin.foreignbooksreader.models.FictionBook;
import ru.mamykin.foreignbooksreader.ui.adapters.viewholders.BookViewHolder;

/**
 * Адаптер с книгами на странице "Мои книги"
 */
public class BooksRecyclerAdapter extends RecyclerView.Adapter<BookViewHolder> {
    private List<FictionBook> booksList;
    private OnBookClickListener listener;
    @Inject
    protected Context context;

    public BooksRecyclerAdapter(OnBookClickListener listener) {
        this.booksList = new ArrayList<>();
        this.listener = listener;
        ReaderApp.getComponent().inject(this);
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View book = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(book, listener);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        final FictionBook item = getItem(position);
        holder.ivBookCover.setImageBitmap(BitmapFactory
                .decodeResource(context.getResources(), R.drawable.img_no_image));
        holder.tvBookTitle.setText(item.getBookTitle());
        holder.tvBookAuthor.setText(item.getBookAuthor());
        holder.vProgress.setPercents(item.getPercents());
        holder.tvBookPages.setText(item.getPagesCountString());
        holder.tvBookAddedDate.setText(item.getLastOpenString());
        UiUtils.setVisibility(holder.vFormat, item.getBookFormat().equals(FictionBook.Format.FBWT));
    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }

    @NonNull
    public FictionBook getItem(int position) {
        return booksList.get(position);
    }

    public void changeData(List<FictionBook> booksList) {
        this.booksList = booksList;
        notifyDataSetChanged();
    }

    public interface OnBookClickListener {
        void onBookClicked(int position);

        void onBookAboutClicked(int position);

        void onBookShareClicked(int position);

        void onBookRemoveClicked(int position);
    }
}