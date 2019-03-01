package ru.mamykin.foboreader.presentation.dropboxbooks.list

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_file.view.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.ui.adapterdelegates.AdapterDelegate
import ru.mamykin.foboreader.domain.entity.DropboxFile

class DropboxDirectoryDelegate(
        private val onClickFunc: (Int) -> Unit
) : AdapterDelegate<DropboxFile>() {

    override fun isForViewType(item: DropboxFile): Boolean = item.isDirectory

    override fun getLayoutId(): Int = R.layout.item_file

    override fun createViewHolder(itemView: View): RecyclerView.ViewHolder =
            DropboxDirectoryViewHolder(itemView)

    override fun bindViewHolder(holder: RecyclerView.ViewHolder, item: DropboxFile) {
        (holder as DropboxDirectoryViewHolder).bind(item, onClickFunc)
    }
}

class DropboxDirectoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(file: DropboxFile, onClickFunc: (Int) -> Unit) = with(itemView) {
        setOnClickListener { onClickFunc(adapterPosition) }
        tvFileName.text = file.name
        ivFileType.setImageResource(R.drawable.ic_folder)
        tvFileAttributes.visibility = View.GONE
    }
}