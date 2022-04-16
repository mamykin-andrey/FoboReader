package ru.mamykin.foboreader.settings.presentation.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.mamykin.foboreader.core.extension.getLayoutInflater
import ru.mamykin.foboreader.core.extension.setCheckedChangedListener
import ru.mamykin.foboreader.core.extension.setColorFilter
import ru.mamykin.foboreader.core.extension.setProgressChangedListener
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
        is SettingsItem.AppLanguage -> R.layout.item_language
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
            R.layout.item_language -> AppLanguageHolder(
                ItemSettingsLanguageBinding.inflate(
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

    // TODO: Support payloads for holders
    private abstract class SettingsItemHolder<T : SettingsItem>(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        abstract fun bind(item: T)
    }

    private class AppLanguageHolder(
        private val binding: ItemSettingsLanguageBinding,
        private val onEvent: (SettingsFeature.Intent) -> Unit,
    ) : SettingsItemHolder<SettingsItem.AppLanguage>(binding.root) {

        init {
            binding.root.setOnClickListener {
                onEvent(SettingsFeature.Intent.SelectAppLanguage)
            }
        }

        override fun bind(item: SettingsItem.AppLanguage) {
            binding.tvName.text = item.languageName
        }
    }

    private class BrightnessHolder(
        private val binding: ItemBackgroundBrightnessBinding,
        private val onEvent: (SettingsFeature.Intent) -> Unit,
    ) : SettingsItemHolder<SettingsItem.Brightness>(binding.root) {

        init {
            binding.seekBright.setProgressChangedListener {
                onEvent(SettingsFeature.Intent.ChangeBrightness(it))
            }
        }

        override fun bind(item: SettingsItem.Brightness) {
            binding.seekBright.progress = item.brightness
        }
    }

    private class NightThemeHolder(
        private val binding: ItemNightThemeBinding,
        private val onEvent: (SettingsFeature.Intent) -> Unit,
    ) : SettingsItemHolder<SettingsItem.NightTheme>(binding.root) {

        init {
            binding.swNightTheme.setCheckedChangedListener {
                onEvent(SettingsFeature.Intent.ChangeNightTheme(it))
            }
        }

        override fun bind(item: SettingsItem.NightTheme) {
            binding.swNightTheme.isChecked = item.isEnabled
        }
    }

    private class TextSizeHolder(
        private val binding: ItemTextSizeBinding,
        private val onEvent: (SettingsFeature.Intent) -> Unit,
    ) : SettingsItemHolder<SettingsItem.ReadTextSize>(binding.root) {

        init {
            binding.btnTextSizePlus.setOnClickListener {
                onEvent(SettingsFeature.Intent.DecreaseTextSize)
            }

            binding.btnTextSizeMinus.setOnClickListener {
                onEvent(SettingsFeature.Intent.IncreaseTextSize)
            }
        }

        override fun bind(item: SettingsItem.ReadTextSize) {
            binding.tvTextSize.text = item.textSize.toString()
        }
    }

    private class TranslationColorHolder(
        private val binding: ItemTranslationColorBinding,
        private val onEvent: (SettingsFeature.Intent) -> Unit,
    ) : SettingsItemHolder<SettingsItem.TranslationColor>(binding.root) {

        init {
            binding.clTranslationColor.setOnClickListener {
                onEvent(SettingsFeature.Intent.SelectReadColor)
            }
        }

        override fun bind(item: SettingsItem.TranslationColor) {
            binding.apply {
                ivTranslationColor.setColorFilter(item.colorCode)
            }
        }
    }

    private class UseVibrationHolder(
        private val binding: ItemUseVibrationBinding,
        private val onEvent: (SettingsFeature.Intent) -> Unit,
    ) : SettingsItemHolder<SettingsItem.UseVibration>(binding.root) {

        init {
            binding.clRoot.setOnClickListener {
                onEvent(SettingsFeature.Intent.ChangeUseVibration(!binding.cbOptionValue.isChecked))
            }
        }

        override fun bind(item: SettingsItem.UseVibration) {
            binding.cbOptionValue.isChecked = item.enabled
        }
    }
}