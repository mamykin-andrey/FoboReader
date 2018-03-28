package ru.mamykin.foboreader.ui.devicebooks.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.data.model.AndroidFile
import java.util.*

class FilesRecyclerAdapter(
        private val onFileClickFunc: (AndroidFile) -> Unit,
        private val onDirClickFunc: (AndroidFile) -> Unit
) : RecyclerView.Adapter<FileViewHolder>() {

    private var files: List<AndroidFile> = Collections.emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
        return FileViewHolder(view, this::onItemClicked)
    }

    private fun onItemClicked(position: Int) {
        val file = getItem(position)
        if (file.isDirectory) {
            onDirClickFunc(file)
        } else {
            onFileClickFunc(file)
        }
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val file = getItem(position)
        holder.bind(file)
    }

    private fun getItem(position: Int): AndroidFile = files[position]

    override fun getItemCount(): Int = files.size

    fun changeData(filesList: List<AndroidFile>) {
        this.files = filesList
        notifyDataSetChanged()
    }
}