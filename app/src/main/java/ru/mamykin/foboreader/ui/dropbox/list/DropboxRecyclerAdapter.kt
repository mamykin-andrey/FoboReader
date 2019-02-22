package ru.mamykin.foboreader.ui.dropbox.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.entity.DropboxFile

class DropboxRecyclerAdapter(
        private val onFileClickFunc: (Int, DropboxFile) -> Unit,
        private val onDirClickFunc: (DropboxFile) -> Unit,
        private val onParentDirClickFunc: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_PARENT_DIR = 0
        const val VIEW_TYPE_FILE = 1
    }

    private var files: List<DropboxFile> = listOf()
    private var loadingItemPos: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
        return when (viewType) {
            VIEW_TYPE_PARENT_DIR -> DropboxParentDirViewHolder(itemView, onParentDirClickFunc)
            VIEW_TYPE_FILE -> DropboxFileViewHolder(itemView, this::onItemClicked)
            else -> throw IllegalStateException("Unknown view type!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (getItemViewType(position)) {
        VIEW_TYPE_PARENT_DIR -> (holder as DropboxParentDirViewHolder).bind()
        else -> (holder as DropboxFileViewHolder).bind(getItem(position), position, loadingItemPos)
    }

    override fun getItemCount(): Int = files.size + 1

    override fun getItemViewType(position: Int): Int = when (position) {
        0 -> VIEW_TYPE_PARENT_DIR
        else -> VIEW_TYPE_FILE
    }

    fun changeData(files: List<DropboxFile>) {
        this.files = files
        notifyDataSetChanged()
    }

    fun showLoadingItem(position: Int) {
        val prevLoadingItemPos = this.loadingItemPos
        loadingItemPos = position
        notifyItemChanged(prevLoadingItemPos)
        notifyItemChanged(loadingItemPos)
    }

    fun hideLoadingItem() {
        val prevLoadingItemPos = loadingItemPos
        loadingItemPos = -1
        notifyItemChanged(prevLoadingItemPos)
    }

    private fun onItemClicked(position: Int) = getItem(position).run {
        if (isDirectory) {
            onDirClickFunc.invoke(this)
        } else {
            onFileClickFunc.invoke(position, this)
        }
    }

    private fun getItem(position: Int): DropboxFile = files[position - 1]
}