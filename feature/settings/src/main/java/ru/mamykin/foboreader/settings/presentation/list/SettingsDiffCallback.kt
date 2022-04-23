package ru.mamykin.foboreader.settings.presentation.list

import androidx.recyclerview.widget.DiffUtil
import ru.mamykin.foboreader.settings.domain.model.SettingsItem

internal class SettingsDiffCallback : DiffUtil.ItemCallback<SettingsItem>() {

    override fun areItemsTheSame(oldItem: SettingsItem, newItem: SettingsItem): Boolean {
        return oldItem::class == newItem::class
    }

    override fun areContentsTheSame(oldItem: SettingsItem, newItem: SettingsItem): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: SettingsItem, newItem: SettingsItem) = Any()
}