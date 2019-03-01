package ru.mamykin.foboreader.presentation.devicebooks.list

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_file.view.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.extension.attributes
import ru.mamykin.foboreader.core.extension.isVisible
import ru.mamykin.foboreader.core.ui.adapterdelegates.AdapterDelegate
import java.io.File

class FileDelegate(
        private val onClickFunc: (Int) -> Unit
) : AdapterDelegate<FileViewHolder, File>() {

    override fun isForViewType(item: File): Boolean = item.isDirectory

    override fun getLayoutId(): Int = R.layout.item_file

    override fun createViewHolder(itemView: View): RecyclerView.ViewHolder =
            FileViewHolder(itemView, onClickFunc)

    override fun bindViewHolder(holder: RecyclerView.ViewHolder, item: File) =
            (holder as FileViewHolder).bind(item)
}

class FileViewHolder(itemView: View,
                     private val onClickFunc: (Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    fun bind(file: File) = with(itemView) {
        setOnClickListener { onClickFunc(adapterPosition) }
        tvFileName.text = file.name
        ivFileType.setImageResource(R.drawable.ic_file)
        tvFileAttributes.isVisible = true
        tvFileAttributes.text = file.attributes
    }
}