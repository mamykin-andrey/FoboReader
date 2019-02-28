package ru.mamykin.foboreader.presentation.dropboxbooks.list

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_file.view.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.extension.attributes
import ru.mamykin.foboreader.core.extension.isFictionBook
import ru.mamykin.foboreader.core.extension.isVisible
import ru.mamykin.foboreader.core.ui.adapterdelegates.AdapterDelegate
import ru.mamykin.foboreader.domain.entity.DropboxFile

class DropboxFictionBookDelegate(
        private val onClickFunc: (Int) -> Unit
) : AdapterDelegate<DropboxFictionBookViewHolder, DropboxFile>() {

    override fun isForViewType(item: Any): Boolean = (item as DropboxFile).isFictionBook

    override fun getLayoutId(): Int = R.layout.item_file

    override fun createViewHolder(itemView: View): RecyclerView.ViewHolder = DropboxFictionBookViewHolder(itemView)

    override fun innerBindViewHolder(holder: DropboxFictionBookViewHolder, item: DropboxFile) = holder.bind(item, onClickFunc)
}

class DropboxFictionBookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(file: DropboxFile, onClickFunc: (Int) -> Unit) = with(itemView) {
        itemView.setOnClickListener { onClickFunc(adapterPosition) }
        tvFileName.text = file.name
        pbLoading.isVisible = false
        ivFileType.setImageResource(R.drawable.ic_book)
        tvFileAttributes.visibility = View.VISIBLE
        tvFileAttributes.text = file.attributes
    }
}