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

class DropboxFileDelegate(
        private val onClickFunc: (Int) -> Unit
) : AdapterDelegate<DropboxFileViewHolder, DropboxFile>() {

    override fun isForViewType(item: Any): Boolean = (item as DropboxFile).let { !it.isDirectory && !it.isFictionBook }

    override fun getLayoutId(): Int = R.layout.item_file

    override fun createViewHolder(itemView: View): RecyclerView.ViewHolder = DropboxFileViewHolder(itemView)

    override fun innerBindViewHolder(holder: DropboxFileViewHolder, item: DropboxFile) = holder.bind(item, onClickFunc)
}

class DropboxFileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(file: DropboxFile, onClickFunc: (Int) -> Unit) = with(itemView) {
        setOnClickListener { onClickFunc(adapterPosition) }
        tvFileName.text = file.name
        pbLoading.isVisible = false
        ivFileType.setImageResource(R.drawable.ic_file)
        tvFileAttributes.visibility = View.VISIBLE
        tvFileAttributes.text = file.attributes
    }
}