package ru.mamykin.foreignbooksreader.ui.adapters.viewholders

import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import butterknife.BindView
import butterknife.ButterKnife
import ru.mamykin.foreignbooksreader.R
import ru.mamykin.foreignbooksreader.ui.adapters.BooksRecyclerAdapter
import ru.mamykin.foreignbooksreader.ui.controls.PercentProgressView

class BookViewHolder(itemView: View, private val listener: BooksRecyclerAdapter.OnBookClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    @BindView(R.id.tvBookTitle)
    var tvBookTitle: TextView? = null
    @BindView(R.id.tvAuthor)
    var tvBookAuthor: TextView? = null
    @BindView(R.id.tvBooksPages)
    var tvBookPages: TextView? = null
    @BindView(R.id.tvBookAddedDate)
    var tvBookAddedDate: TextView? = null
    @BindView(R.id.pvProgress)
    var vProgress: PercentProgressView? = null
    @BindView(R.id.ivBookCover)
    var ivBookCover: ImageView? = null
    @BindView(R.id.btnMenu)
    var btnMenu: View? = null
    @BindView(R.id.tvFormat)
    var vFormat: View? = null

    private val menuListener = { item ->
        when (item.getItemId()) {
            R.id.menu_about_book -> listener.onBookAboutClicked(adapterPosition)
            R.id.menu_share_book -> listener.onBookShareClicked(adapterPosition)
            R.id.menu_remove_book -> listener.onBookRemoveClicked(adapterPosition)
        }
        true
    }

    init {
        ButterKnife.bind(this, itemView)
        itemView.setOnClickListener(this)
        btnMenu!!.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.btnMenu) {
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.setOnMenuItemClickListener(menuListener)
            popupMenu.inflate(R.menu.menu_book_item)
            popupMenu.show()
        } else {
            listener.onBookClicked(adapterPosition)
        }
    }
}