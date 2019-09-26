package ru.mamykin.foboreader.presentation.devicebooks.list

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_file.view.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.extension.attributes
import ru.mamykin.foboreader.core.extension.isFictionBook
import ru.mamykin.foboreader.core.extension.isVisible
import ru.mamykin.foboreader.core.ui.adapterdelegates.AdapterDelegate
import java.io.File

class FictionBookDelegate(
        private val onClickFunc: (Int) -> Unit
) : AdapterDelegate<File>() {

    override fun isForViewType(item: File) = item.isFictionBook

    override fun getLayoutId(): Int = R.layout.item_file

    override fun createViewHolder(itemView: View): RecyclerView.ViewHolder =
            FictionBookViewHolder(itemView, onClickFunc)

    override fun bindViewHolder(holder: RecyclerView.ViewHolder, item: File) {
        (holder as FictionBookViewHolder).bind(item)
    }
}

class FictionBookViewHolder(itemView: View,
                            private val onClickFunc: (Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    fun bind(file: File) = with(itemView) {
        setOnClickListener { onClickFunc(adapterPosition) }
        tvFileName.text = file.name
        ivFileType.setImageResource(R.drawable.ic_book)
        tvFileAttributes.isVisible = true
        tvFileAttributes.text = file.attributes
    }
}