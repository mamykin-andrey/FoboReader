package ru.mamykin.foreignbooksreader.ui.devicebooks.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList

import javax.inject.Inject

import ru.mamykin.foreignbooksreader.R
import ru.mamykin.foreignbooksreader.ReaderApp
import ru.mamykin.foreignbooksreader.models.AndroidFile
import ru.mamykin.foreignbooksreader.ui.global.FileViewHolder

/**
 * Адаптер с файлами на странице "Устройство"
 */
class FilesRecyclerAdapter(private val listener: FileViewHolder.OnItemClickListener) : RecyclerView.Adapter<FileViewHolder>() {
    private var filesList: List<AndroidFile>? = null
    @Inject
    lateinit var context: Context

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
        val file = getItem(position)
        holder.tvFileName!!.text = file.name
        if (file.isDirectory) {
            holder.ivFileType!!.setImageResource(R.drawable.ic_folder)
            holder.tvFileAttributes!!.visibility = View.GONE
        } else if (file.isFictionBook) {
            holder.ivFileType!!.setImageResource(R.drawable.ic_book)
            holder.tvFileAttributes!!.visibility = View.VISIBLE
            holder.tvFileAttributes!!.text = file.attributes
        } else {
            holder.ivFileType!!.setImageResource(R.drawable.ic_file)
            holder.tvFileAttributes!!.visibility = View.VISIBLE
            holder.tvFileAttributes!!.text = file.attributes
        }
    }

    fun getItem(position: Int): AndroidFile {
        return filesList!![position]
    }

    override fun getItemCount(): Int {
        return filesList!!.size
    }

    fun changeData(filesList: List<AndroidFile>) {
        this.filesList = filesList
        notifyDataSetChanged()
    }
}