package ru.mamykin.foboreader.presentation.booksstore.list

import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_featured_book.view.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.extension.isVisible
import ru.mamykin.foboreader.core.ui.adapterdelegates.AdapterDelegate
import ru.mamykin.foboreader.domain.entity.StoreBook

class FeaturedBookDelegate : AdapterDelegate<Any>() {

    override fun getLayoutId(): Int = R.layout.item_featured_book

    override fun isForViewType(item: Any): Boolean = item is StoreBook

    override fun createViewHolder(itemView: View): RecyclerView.ViewHolder =
            FeaturedBookViewHolder(itemView)

    override fun bindViewHolder(holder: RecyclerView.ViewHolder, item: Any) {
        (holder as FeaturedBookViewHolder).bind(item as StoreBook)
    }
}

class FeaturedBookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(book: StoreBook) = with(itemView) {
        tvName.text = book.title
        tvAuthor.text = book.author
        tvCategory.text = book.genre
        tvRating.text = book.rating.toString()
        tvPrice.text = context.getString(R.string.book_price_format, book.price)
        tvOldPrice.text = context.getString(R.string.book_price_format, book.oldPrice)
        tvOldPrice.isVisible = book.oldPrice != null
        tvOldPrice.paintFlags = tvOldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        Picasso.with(context)
                .load(book.pictureUrl)
                .resize(500, 500)
                .placeholder(R.drawable.img_no_image)
                .into(ivCover)
    }
}