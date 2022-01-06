package ru.mamykin.foboreader.core.extension

import android.view.*
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import kotlinx.coroutines.flow.*

fun View.showPopupMenu(@MenuRes menuRes: Int, vararg clicks: Pair<Int, () -> Unit>) {
    PopupMenu(context, this).apply {
        setOnMenuItemClickListener {
            clicks.find { (id, _) -> it.itemId == id }?.second?.invoke()
            return@setOnMenuItemClickListener true
        }
        inflate(menuRes)
        show()
    }
}

fun SeekBar.setProgressChangedListener(listener: (Int) -> Unit) {
    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            if (fromUser) {
                listener.invoke(progress)
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {}
        override fun onStopTrackingTouch(seekBar: SeekBar) {}
    })
}

fun Switch.setCheckedChangedListener(listener: (Boolean) -> Unit) {
    setOnCheckedChangeListener { _, isChecked ->
        listener.invoke(isChecked)
    }
}

fun Menu.getSearchView(@IdRes itemId: Int): SearchView {
    val searchItem = findItem(itemId)
    return searchItem.actionView as SearchView
}

fun SearchView.setQueryChangedListener(listener: (String) -> Unit) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            listener(newText.orEmpty())
            return true
        }
    })
}

fun MenuItem.setItemClickedListener(listener: () -> Boolean) {
    setOnMenuItemClickListener { listener() }
}

/**
 * Set text to the textview, or set gone, if text null or blank
 */
fun TextView.setTextOrGone(text: String?) {
    if (!text.isNullOrBlank()) {
        isVisible = true
        this.text = text
    } else {
        isVisible = false
    }
}

fun ViewGroup.getLayoutInflater(): LayoutInflater = LayoutInflater.from(context)