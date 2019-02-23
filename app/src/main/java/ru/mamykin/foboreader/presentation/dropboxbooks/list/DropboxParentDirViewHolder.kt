package ru.mamykin.foboreader.presentation.dropboxbooks.list

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_file.view.*
import ru.mamykin.foboreader.R

// TODO: remove
class DropboxParentDirViewHolder(
        itemView: View,
        private val onClickFunc: () -> Unit
) : RecyclerView.ViewHolder(itemView) {

    fun bind() = with(itemView) {
        tvFileName.text = itemView.context.getString(R.string.navigate_up)
        ivFileType.setImageResource(R.drawable.ic_back)
        tvFileAttributes.visibility = View.GONE
        setOnClickListener { onClickFunc() }
    }
}