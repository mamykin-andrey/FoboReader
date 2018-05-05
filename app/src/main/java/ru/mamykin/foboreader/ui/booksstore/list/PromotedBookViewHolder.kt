package ru.mamykin.foboreader.ui.booksstore.list

import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_store_book.view.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.entity.StoreBook
import ru.mamykin.foboreader.extension.isVisible

class PromotedBookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(book: StoreBook) {
        itemView.tvName.text = book.title
        itemView.tvAuthor.text = book.author
        itemView.tvCategory.text = book.genre
        itemView.tvRating.text = book.ratingStr
        itemView.tvPrice.text = book.priceStr
        itemView.tvOldPrice.text = book.oldPriceStr
        itemView.tvOldPrice.isVisible = book.oldPrice != null
        itemView.tvOldPrice.paintFlags = itemView.tvOldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        Picasso.with(itemView.context)
                .load(book.pictureUrl)
                .resize(500, 500)
                .placeholder(R.drawable.img_no_image)
                .into(itemView.ivCover)
    }
}