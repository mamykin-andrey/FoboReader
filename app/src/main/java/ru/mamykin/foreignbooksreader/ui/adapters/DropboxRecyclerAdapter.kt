package ru.mamykin.foreignbooksreader.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList

import javax.inject.Inject

import ru.mamykin.foreignbooksreader.R
import ru.mamykin.foreignbooksreader.ReaderApp
import ru.mamykin.foreignbooksreader.models.DropboxFile
import ru.mamykin.foreignbooksreader.ui.adapters.viewholders.FileViewHolder

/**
 * Адаптер с файлами на странице "Dropbox"
 */
class DropboxRecyclerAdapter(private val listener: FileViewHolder.OnItemClickListener) : RecyclerView.Adapter<FileViewHolder>() {
    private var filesList: List<DropboxFile> = ArrayList(0)
    @Inject
    lateinit var context: Context
    private var loadingItem = -1

    init {
        filesList = ArrayList()
        ReaderApp.component.inject(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(
                parent.context).inflate(R.layout.item_file, parent, false)
        return FileViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val item = getItem(position)
        if (position == loadingItem) {
            // Загружаемый элемент
            holder.pbLoading!!.visibility = View.VISIBLE
        } else {
            holder.tvFileName!!.text = item.name
            holder.pbLoading!!.visibility = View.GONE
            if (item.isDirectory) {
                // Папка
                holder.ivFileType!!.setImageResource(R.drawable.ic_folder)
                holder.tvFileAttributes!!.visibility = View.GONE
            } else if (item.isFictionBook) {
                // Fiction Book
                holder.ivFileType!!.setImageResource(R.drawable.ic_book)
                holder.tvFileAttributes!!.visibility = View.VISIBLE
                holder.tvFileAttributes!!.text = item.attributes
            } else {
                // Обычный файл
                holder.ivFileType!!.setImageResource(R.drawable.ic_file)
                holder.tvFileAttributes!!.visibility = View.VISIBLE
                holder.tvFileAttributes!!.text = item.attributes
            }
        }
    }

    fun getItem(position: Int): DropboxFile {
        return filesList[position]
    }

    override fun getItemCount(): Int {
        return filesList.size
    }

    fun changeData(files: List<DropboxFile>) {
        filesList = files
        notifyDataSetChanged()
    }

    fun showLoadingItem(position: Int) {
        this.loadingItem = position
        notifyDataSetChanged()
    }

    fun hideLoadingItem() {
        this.loadingItem = -1
        notifyDataSetChanged()
    }
}