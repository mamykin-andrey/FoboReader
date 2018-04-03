package ru.mamykin.foboreader.ui.booksstore.list

import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_store_book.view.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.entity.StoreBook
import ru.mamykin.foboreader.extension.isVisible

class StoreBookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(book: StoreBook) {
        itemView.tvName.text = book.title
        itemView.tvAuthor.text = book.author
        itemView.tvCategory.text = book.genre
        itemView.tvRating.text = book.ratingStr
        itemView.tvPrice.text = book.price
        itemView.tvOldPrice.text = book.oldPrice
        itemView.tvOldPrice.isVisible = book.oldPrice != null
        itemView.tvOldPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        Picasso.with(itemView.context)
                .load(book.pictureUrl)
                .placeholder(R.drawable.img_no_image)
                .into(itemView.ivCover)
    }
}