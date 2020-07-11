package ru.mamykin.foboreader.core.extension

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SwitchCompat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import reactivecircus.flowbinding.android.widget.SeekBarChangeEvent
import reactivecircus.flowbinding.android.widget.changeEvents
import reactivecircus.flowbinding.android.widget.checkedChanges

var View.isVisible: Boolean
    set(value) {
        this.visibility = if (value) View.VISIBLE else View.GONE
    }
    get() = this.visibility == View.VISIBLE

fun ViewGroup.inflateView(@LayoutRes resId: Int, attach: Boolean = false) =
    LayoutInflater.from(this.context).inflate(resId, this, attach)

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

fun SeekBar.changeProgressEvents(emitImmediately: Boolean = false): Flow<Int> =
    changeEvents(emitImmediately)
        .filter { it is SeekBarChangeEvent.ProgressChanged && it.fromUser }
        .map { it.view.progress }

fun SwitchCompat.manualCheckedChanges(): Flow<Boolean> =
    checkedChanges()
        .filter { this.isPressed }