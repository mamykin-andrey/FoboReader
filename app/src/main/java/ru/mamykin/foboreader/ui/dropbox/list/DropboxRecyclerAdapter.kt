package ru.mamykin.foboreader.ui.dropbox.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.data.model.DropboxFile

class DropboxRecyclerAdapter(
        private val onFileClickFunc: (DropboxFile) -> Unit,
        private val onDirClickFunc: (DropboxFile) -> Unit
) : RecyclerView.Adapter<DropboxFileViewHolder>() {

    private var files: List<DropboxFile> = listOf()
    private var loadingItemPos = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DropboxFileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
        return DropboxFileViewHolder(view, this::onItemClicked)
    }

    override fun onBindViewHolder(holder: DropboxFileViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position, loadingItemPos)
    }

    override fun getItemCount(): Int {
        return files.size
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

    private fun onItemClicked(position: Int) {
        val item = getItem(position)
        if (item.isDirectory) {
            onDirClickFunc(item)
        } else {
            onFileClickFunc(item)
        }
    }

    private fun getItem(position: Int): DropboxFile {
        return files[position]
    }
}