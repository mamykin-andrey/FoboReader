package ru.mamykin.foboreader.settings.presentation.list

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import ru.mamykin.foboreader.settings.databinding.ItemTranslationColorBinding
import ru.mamykin.foboreader.settings.domain.model.SettingsItem
import ru.mamykin.foboreader.settings.presentation.Settings

internal class TranslationColorHolder(
    private val binding: ItemTranslationColorBinding,
    private val onEvent: (Settings.Event) -> Unit,
) : SettingsItemHolder<SettingsItem.TranslationColor>(binding.root) {

    init {
        binding.clTranslationColor.setOnClickListener {
            onEvent(Settings.Event.SelectReadColorClicked)
        }
    }

    override fun bind(item: SettingsItem.TranslationColor) {
        binding.apply {
            ivTranslationColor.colorFilter = PorterDuffColorFilter(
                Color.parseColor(item.colorCode),
                PorterDuff.Mode.SRC_ATOP
            )
        }
    }
}