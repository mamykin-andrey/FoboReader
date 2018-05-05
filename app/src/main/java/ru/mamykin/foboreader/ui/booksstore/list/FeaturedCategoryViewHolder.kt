package ru.mamykin.foboreader.ui.booksstore.list

import android.support.v7.widget.RecyclerView
import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_featured_book.view.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.entity.booksstore.FeaturedCategory

class FeaturedCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(category: FeaturedCategory) {
        itemView.tvTitle.text = category.title
        itemView.tvSubtitle.text = category.subtitle
        Picasso.with(itemView.context)
                .load(category.pictureUrl)
                .resize(500, 500)
                .placeholder(R.drawable.img_no_image)
                .into(itemView.ivCover)
    }
}