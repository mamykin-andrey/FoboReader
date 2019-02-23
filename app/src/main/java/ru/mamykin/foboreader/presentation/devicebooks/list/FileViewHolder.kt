package ru.mamykin.foboreader.presentation.devicebooks.list

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_file.view.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.extension.attributes
import ru.mamykin.foboreader.core.extension.isFictionBook
import ru.mamykin.foboreader.core.extension.isVisible
import java.io.File

// TODO: refactor
class FileViewHolder(itemView: View,
                     private val onItemClickFunc: (Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    fun bind(file: File) = with(itemView) {
        setOnClickListener { onItemClickFunc(adapterPosition) }
        tvFileName.text = file.name
        when {
            file.isDirectory -> {
                ivFileType.setImageResource(R.drawable.ic_folder)
                tvFileAttributes.isVisible = false
            }
            file.isFictionBook -> {
                ivFileType.setImageResource(R.drawable.ic_book)
                tvFileAttributes.isVisible = true
                tvFileAttributes.text = file.attributes
            }
            else -> {
                ivFileType.setImageResource(R.drawable.ic_file)
                tvFileAttributes.isVisible = true
                tvFileAttributes.text = file.attributes
            }
        }
    }
}