package ru.mamykin.foboreader.settings.presentation.list

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.mamykin.foboreader.core.extension.getLayoutInflater
import ru.mamykin.foboreader.core.presentation.list.SimpleDiffUtil
import ru.mamykin.foboreader.settings.R
import ru.mamykin.foboreader.settings.databinding.*
import ru.mamykin.foboreader.settings.domain.model.SettingsItem
import ru.mamykin.foboreader.settings.presentation.SettingsFeature

internal class SettingsListAdapter(
    private val onEvent: (SettingsFeature.Intent) -> Unit,
) : ListAdapter<SettingsItem, RecyclerView.ViewHolder>(
    SimpleDiffUtil.equalsCallback { it::class.java }
) {
    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is SettingsItem.NightTheme -> R.layout.item_night_theme
        is SettingsItem.Brightness -> R.layout.item_background_brightness
        is SettingsItem.ReadTextSize -> R.layout.item_text_size
        is SettingsItem.TranslationColor -> R.layout.item_translation_color
        is SettingsItem.AppLanguage -> R.layout.item_app_language
        is SettingsItem.UseVibration -> R.layout.item_use_vibration
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.item_night_theme -> NightThemeHolder(
                ItemNightThemeBinding.inflate(
                    parent.getLayoutInflater(),
                    parent,
                    false
                ),
                onEvent,
            )
            R.layout.item_background_brightness -> BrightnessHolder(
                ItemBackgroundBrightnessBinding.inflate(
                    parent.getLayoutInflater(),
                    parent,
                    false
                ),
                onEvent,
            )
            R.layout.item_text_size -> TextSizeHolder(
                ItemTextSizeBinding.inflate(
                    parent.getLayoutInflater(),
                    parent,
                    false
                ),
                onEvent,
            )
            R.layout.item_translation_color -> TranslationColorHolder(
                ItemTranslationColorBinding.inflate(
                    parent.getLayoutInflater(),
                    parent,
                    false
                ),
                onEvent,
            )
            R.layout.item_app_language -> AppLanguageHolder(
                ItemAppLanguageBinding.inflate(
                    parent.getLayoutInflater(),
                    parent,
                    false
                ),
                onEvent,
            )
            R.layout.item_use_vibration -> UseVibrationHolder(
                ItemUseVibrationBinding.inflate(
                    parent.getLayoutInflater(),
                    parent,
                    false
                ),
                onEvent,
            )
            else -> throw IllegalArgumentException("Unknown viewType: $viewType!")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        @Suppress("unchecked_cast")
        val holders = holder as SettingsItemHolder<SettingsItem>
        holders.bind(getItem(position))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
    }
}