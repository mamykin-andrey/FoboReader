package ru.mamykin.foreignbooksreader.ui.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import butterknife.BindView
import butterknife.ButterKnife
import ru.mamykin.foreignbooksreader.R

class StoreBookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    @BindView(R.id.tvName)
    var tvBookName: TextView? = null
    @BindView(R.id.tvAuthor)
    var tvBookAuthor: TextView? = null
    @BindView(R.id.tvCategory)
    var tvBookCategory: TextView? = null
    @BindView(R.id.tvPrice)
    var tvBookPrice: TextView? = null
    @BindView(R.id.tvOldPrice)
    var tvBookOldPrice: TextView? = null
    @BindView(R.id.tvRating)
    var tvRating: TextView? = null
    @BindView(R.id.ivCover)
    var ivCover: ImageView? = null

    init {
        ButterKnife.bind(this, itemView)
    }
}