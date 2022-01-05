package ru.mamykin.foboreader.settings.presentation.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.mamykin.foboreader.settings.domain.model.SettingsItem

// TODO: Support payloads for holders
abstract class SettingsItemHolder<T : SettingsItem>(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(item: T)
}