package ru.mamykin.foboreader.ui.devicebooks.list

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_file.view.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.extension.attributes
import ru.mamykin.foboreader.extension.isFictionBook
import java.io.File

class FileViewHolder(itemView: View,
                     onItemClickFunc: (Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    init {
        itemView.setOnClickListener { onItemClickFunc(adapterPosition) }
    }

    fun bind(file: File) {
        itemView.tvFileName.text = file.name
        if (file.isDirectory) {
            itemView.ivFileType.setImageResource(R.drawable.ic_folder)
            itemView.tvFileAttributes.visibility = View.GONE
        } else if (file.isFictionBook) {
            itemView.ivFileType.setImageResource(R.drawable.ic_book)
            itemView.tvFileAttributes.visibility = View.VISIBLE
            itemView.tvFileAttributes.text = file.attributes
        } else {
            itemView.ivFileType.setImageResource(R.drawable.ic_file)
            itemView.tvFileAttributes.visibility = View.VISIBLE
            itemView.tvFileAttributes.text = file.attributes
        }
    }
}