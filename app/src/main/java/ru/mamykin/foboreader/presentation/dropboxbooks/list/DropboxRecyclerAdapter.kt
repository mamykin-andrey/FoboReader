package ru.mamykin.foboreader.presentation.dropboxbooks.list

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import ru.mamykin.foboreader.core.ui.adapterdelegates.AdapterDelegatesManager
import ru.mamykin.foboreader.domain.entity.DropboxFile

class DropboxRecyclerAdapter(
        private val onFileClickFunc: (DropboxFile) -> Unit,
        private val onDirClickFunc: (DropboxFile) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<DropboxFile> = listOf()
    private val delegatesManager = AdapterDelegatesManager()

    init {
        delegatesManager.addDelegate(DropboxDirectoryDelegate { onDirClickFunc(items[it]) })
        delegatesManager.addDelegate(DropboxFictionBookDelegate { onFileClickFunc(items[it]) })
        delegatesManager.addDelegate(DropboxFileDelegate { onFileClickFunc(items[it]) })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            delegatesManager.createViewHolder(parent, viewType)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegatesManager.onBindViewHolder(items, holder, position)
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = delegatesManager.getItemViewType(items, position)

    fun changeData(items: List<DropboxFile>) {
        this.items = items
        notifyDataSetChanged()
    }
}