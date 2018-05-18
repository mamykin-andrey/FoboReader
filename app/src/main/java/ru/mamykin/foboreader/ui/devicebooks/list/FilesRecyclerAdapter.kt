package ru.mamykin.foboreader.ui.devicebooks.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.mamykin.foboreader.R
import java.io.File
import java.util.*

class FilesRecyclerAdapter(
        private val onFileClickFunc: (File) -> Unit,
        private val onDirClickFunc: (File) -> Unit
) : RecyclerView.Adapter<FileViewHolder>() {

    private var files: List<File> = Collections.emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
        return FileViewHolder(view, this::onItemClicked)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val file = getItem(position)
        holder.bind(file)
    }

    override fun getItemCount(): Int = files.size

    fun changeData(files: List<File>) {
        this.files = files
        notifyDataSetChanged()
    }

    private fun getItem(position: Int): File = files[position]

    private fun onItemClicked(position: Int) {
        val item = getItem(position)
        when {
            item.isDirectory -> onDirClickFunc(item)
            else -> onFileClickFunc(item)
        }
    }
}