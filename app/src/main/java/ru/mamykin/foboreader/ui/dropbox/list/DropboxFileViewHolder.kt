package ru.mamykin.foboreader.ui.dropbox.list

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_file.view.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.entity.DropboxFile
import ru.mamykin.foboreader.extension.attributes
import ru.mamykin.foboreader.extension.isFictionBook

class DropboxFileViewHolder(itemView: View,
                            onItemClickFunc: (Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    init {
        itemView.setOnClickListener { onItemClickFunc(adapterPosition) }
    }

    fun bind(file: DropboxFile, pos: Int, loadingItemPos: Int) {
        if (pos == loadingItemPos) {
            itemView.pbLoading.visibility = View.VISIBLE
        } else {
            itemView.tvFileName.text = file.name
            itemView.pbLoading.visibility = View.GONE
            if (file.isDirectory) {
                // Папка
                itemView.ivFileType.setImageResource(R.drawable.ic_folder)
                itemView.tvFileAttributes.visibility = View.GONE
            } else if (file.isFictionBook) {
                // Fiction Book
                itemView.ivFileType.setImageResource(R.drawable.ic_book)
                itemView.tvFileAttributes.visibility = View.VISIBLE
                itemView.tvFileAttributes.text = file.attributes
            } else {
                // Обычный файл
                itemView.ivFileType.setImageResource(R.drawable.ic_file)
                itemView.tvFileAttributes.visibility = View.VISIBLE
                itemView.tvFileAttributes.text = file.attributes
            }
        }
    }
}