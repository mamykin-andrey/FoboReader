package ru.mamykin.foboreader.settings.presentation.list

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks
import ru.mamykin.foboreader.settings.databinding.ItemTranslationColorBinding
import ru.mamykin.foboreader.settings.domain.model.SettingsItem
import ru.mamykin.foboreader.settings.presentation.Settings

class TranslationColorHolder(
    private val binding: ItemTranslationColorBinding,
    private val onEvent: (Settings.Event) -> Unit,
    lifecycleScope: CoroutineScope
) : SettingsItemHolder<SettingsItem.TranslationColor>(binding.root) {

    init {
        binding.clTranslationColor.clicks()
            .onEach { onEvent(Settings.Event.SelectReadColorClicked) }
            .launchIn(lifecycleScope)
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