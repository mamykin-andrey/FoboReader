package ru.mamykin.foboreader.ui.dropbox.list

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_file.view.*
import ru.mamykin.foboreader.R

class DropboxParentDirViewHolder(
        itemView: View,
        private val onClickFunc: () -> Unit) : RecyclerView.ViewHolder(itemView) {

    fun bind() {
        itemView.tvFileName.text = "Вверх..."
        itemView.ivFileType.setImageResource(R.drawable.ic_back)
        itemView.tvFileAttributes.visibility = View.GONE
        itemView.setOnClickListener { onClickFunc() }
    }
}