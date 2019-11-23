package ru.mamykin.foboreader.presentation.devicebooks.list

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import ru.mamykin.core.ui.adapterdelegates.AdapterDelegatesManager
import java.io.File

class FilesRecyclerAdapter(
        onFileClickFunc: (File) -> Unit,
        onDirClickFunc: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<File> = listOf()
    private val delegatesManager = AdapterDelegatesManager<File>()

    init {
        delegatesManager.addDelegate(DirectoryDelegate { onDirClickFunc(items[it].absolutePath) })
        delegatesManager.addDelegate(FileDelegate { onFileClickFunc(items[it]) })
        delegatesManager.addDelegate(FictionBookDelegate { onFileClickFunc(items[it]) })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            delegatesManager.createViewHolder(parent, viewType)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegatesManager.bindViewHolder(items, holder, position)
    }

    override fun getItemCount(): Int = items.size

    fun changeData(files: List<File>) {
        this.items = files
        notifyDataSetChanged()
    }
}